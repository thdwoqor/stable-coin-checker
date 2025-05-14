package org.example.stablecoinchecker.chart.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.application.dto.CandlestickResponse;
import org.example.stablecoinchecker.chart.domain.Candlestick;
import org.example.stablecoinchecker.chart.domain.CandlestickRepository;
import org.example.stablecoinchecker.chart.domain.CryptoExchange;
import org.example.stablecoinchecker.chart.domain.Identifier;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandlestickService {

    private final CandlestickRepository candlestickRepository;
    private final RedisTemplate<Identifier, BigDecimal> priceRedisTemplate;
    private final RedisTemplate<String, Identifier> indexRedisTemplate;

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

        Set<Identifier> identifiers = indexRedisTemplate.opsForZSet().range("index", 0, -1);
        for (Identifier identifier : identifiers) {
            if (!identifier.isSameAs(CryptoExchange.from(cryptoExchange), symbol, TimeInterval.valueOf(interval))) {
                continue;
            }

            Set<BigDecimal> prices = priceRedisTemplate.opsForZSet().range(identifier, 0, -1);
            if (prices == null || prices.isEmpty()) {
                continue;
            }
            activeCandlestick.add(Candlestick.create(identifier, prices));
        }

        return Stream.of(candlesticks, activeCandlestick)
                .flatMap(List::stream)
                .map(CandlestickResponse::from)
                .sorted((c1, c2) -> Long.compare(c2.time(), c1.time()))
                .toList();
    }

}
