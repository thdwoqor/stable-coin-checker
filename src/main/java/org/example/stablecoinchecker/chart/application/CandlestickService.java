package org.example.stablecoinchecker.chart.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.application.dto.CandlestickResponse;
import org.example.stablecoinchecker.chart.domain.Candlestick;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CandlestickRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandlestickService {

    private final CandlestickRepository candlestickRepository;
    private final RedisTemplate<String, String> indexRedisTemplate;
    private final RedisTemplate<String, BigDecimal> priceRedisTemplate;

    public List<CandlestickResponse> getKlineData(
            String cryptoExchange,
            String symbol,
            String interval,
            long endTime,
            int limit
    ) {
        List<Candlestick> candlesticks = candlestickRepository.findCandlesticks(
                cryptoExchange,
                symbol,
                interval,
                endTime,
                limit
        );

        List<Candlestick> activeCandlestick = new ArrayList<>();
        String prefix = String.format("%s:%s:%s", cryptoExchange, symbol, interval);
        Set<String> keys = indexRedisTemplate.opsForZSet().range("index", 0, -1);
        for (String key : keys) {
            if (isNotSamePrefix(key, prefix)) {
                continue;
            }

            Set<BigDecimal> prices = priceRedisTemplate.opsForZSet().range(key, 0, -1);
            if (prices == null || prices.isEmpty()) {
                continue;
            }
            activeCandlestick.add(Candlestick.create(CandlestickId.from(key), prices));
        }

        return Stream.of(candlesticks, activeCandlestick)
                .flatMap(List::stream)
                .map(CandlestickResponse::from)
                .sorted((c1, c2) -> Long.compare(c2.time(), c1.time()))
                .toList();
    }

    private boolean isNotSamePrefix(final String key, final String prefix) {
        String[] parts = key.split(":");
        String join = String.join(":", parts[0], parts[1], parts[2]);
        return !join.equals(prefix);
    }

}
