package org.example.stablecoinchecker.chart.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.domain.Candlestick;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CandlestickRepository;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandlestickScheduler {

    private final RedisTemplate<String, BigDecimal> priceRedisTemplate;
    private final RedisTemplate<String, String> indexRedisTemplate;
    private final CandlestickRepository candlestickRepository;

    /*
        비활성 상태의 캔들을 영구 저장하는 스케줄러
        예: 5분봉 차트를 생성할 때, 현재 시간이 1시 11분이라면 1시 05분까지의 캔들은 이미 완성된 상태입니다.
        따라서 이들은 '비활성 캔들'로 간주되어 영구 저장 대상이 됩니다.
     */
    @Scheduled(cron = "5 * * * * *")
    public void batchCandleData() {
        long now = Instant.now().toEpochMilli();
        ZSetOperations<String, String> indexOps = indexRedisTemplate.opsForZSet();

        Set<String> keys = indexOps.rangeByScore("index", 0, now);

        for (String key : keys) {
            String[] parts = key.split(":");
            TimeInterval interval = TimeInterval.valueOf(parts[2]);
            long timestamp = Long.parseLong(parts[3]);

            if (!isComplete(timestamp, interval, now)) {
                continue;
            }

            Set<BigDecimal> prices = priceRedisTemplate.opsForZSet().range(key, 0, -1);
            if (prices == null || prices.isEmpty()) {
                continue;
            }

            Candlestick candlestick = Candlestick.create(CandlestickId.from(key), prices);
            candlestickRepository.save(candlestick);

            priceRedisTemplate.delete(key);
            indexOps.remove("index", key);
        }
    }

    private boolean isComplete(long windowStart, TimeInterval interval, long now) {
        long currentWindow = TimeInterval.calculateTimestamp(interval, now);
        if (windowStart == currentWindow) {
            return false;
        }
        return true;
    }

}
