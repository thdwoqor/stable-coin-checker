package org.example.stablecoinchecker.chart.application;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchangePriceEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveCandlestickGenerator {

    private final RedisTemplate<String, BigDecimal> priceRedisTemplate;
    private final RedisTemplate<String, String> indexRedisTemplate;

    /*
        - 웹소켓으로 제공 받은 가격 데이터를 모두 Redis Sorted Set 에 저장한다.
          시가-종가을 정보가 중요하기때문에 Sorted Set 사용

        - 이후 스케줄러에서 key 를 쉽게 찾기 위해서 indexOps 사용
          key 를 timestamp 기반으로 정렬해서 저장하고있으면 이후에 timestamp 범위로 key 를 빠르게 찾을 수 있다.
     */
    @EventListener
    @Async("candlestickGeneratorAsyncExecutor")
    public void consumePriceEvent(CryptoExchangePriceEvent event) {
        ZSetOperations<String, BigDecimal> priceOps = priceRedisTemplate.opsForZSet();
        ZSetOperations<String, String> indexOps = indexRedisTemplate.opsForZSet();

        long eventTime = event.timestamp();
        for (TimeInterval interval : TimeInterval.values()) {
            long timestamp = TimeInterval.calculateTimestamp(interval, eventTime);
            String key = String.format("%s:%s:%s:%s",
                    event.identifier(),
                    event.symbol(),
                    interval.name(),
                    timestamp);

            priceOps.add(key, event.price(), eventTime);
            indexOps.add("index", key, timestamp);
        }
    }

}
