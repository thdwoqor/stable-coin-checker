package org.example.stablecoinchecker.chart.application;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.domain.CryptoExchange;
import org.example.stablecoinchecker.chart.domain.Identifier;
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

    private final RedisTemplate<Identifier, BigDecimal> priceRedisTemplate;
    private final RedisTemplate<String, Identifier> indexRedisTemplate;

    /*
        - 웹소켓으로 제공 받은 가격 데이터를 모두 Redis Sorted Set 에 저장한다.
          시가-종가을 정보가 중요하기때문에 Sorted Set 사용

        - 이후 스케줄러에서 key 를 쉽게 찾기 위해서 indexOps 사용
          key 를 timestamp 기반으로 정렬해서 저장하고있으면 이후에 timestamp 범위로 key 를 빠르게 찾을 수 있다.
     */
    @EventListener
    @Async("candlestickGeneratorAsyncExecutor")
    public void consumePriceEvent(CryptoExchangePriceEvent event) {
        ZSetOperations<Identifier, BigDecimal> priceOps = priceRedisTemplate.opsForZSet();
        ZSetOperations<String, Identifier> indexOps = indexRedisTemplate.opsForZSet();

        long eventTime = event.timestamp();
        for (TimeInterval interval : TimeInterval.values()) {
            Identifier identifier = toIdentifier(event, interval);
            priceOps.add(identifier, event.price(), eventTime);
            indexOps.add("index", identifier, event.timestamp());
        }
    }

    private Identifier toIdentifier(final CryptoExchangePriceEvent event, final TimeInterval interval) {
        return Identifier.from(
                CryptoExchange.from(event.cryptoExchange()),
                event.symbol(),
                interval,
                TimeInterval.calculateTimestamp(interval, event.timestamp())
        );
    }

}
