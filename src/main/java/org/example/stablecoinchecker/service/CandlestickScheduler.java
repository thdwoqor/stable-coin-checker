package org.example.stablecoinchecker.service;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.domain.candlestick.Candlestick;
import org.example.stablecoinchecker.domain.candlestick.CandlestickId;
import org.example.stablecoinchecker.domain.candlestick.CandlestickRepository;
import org.example.stablecoinchecker.domain.candlestick.RedisCandlestick;
import org.example.stablecoinchecker.domain.candlestick.RedisCandlestickRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandlestickScheduler {

    private final RedisCandlestickRepository redisCandlestickRepository;
    private final CandlestickRepository candlestickRepository;

    @Scheduled(cron = "3 */5 * * * *")
    public void batchCandleData() {
        List<RedisCandlestick> redisCandlesticks = redisCandlestickRepository.findAll();

        for (RedisCandlestick redisCandlestick : redisCandlesticks) {
            CandlestickId candlestickId = CandlestickId.deserialized(redisCandlestick.getId());
            if (candlestickId.getTimestamp() < Instant.now().toEpochMilli()) {
                candlestickRepository.save(toCandlestick(redisCandlestick, candlestickId));
                redisCandlestickRepository.deleteById(redisCandlestick.getId());
            }
        }
    }

    private Candlestick toCandlestick(
            final RedisCandlestick redisCandlestick,
            final CandlestickId candlestickId
    ) {
        return Candlestick.create(
                candlestickId,
                redisCandlestick.getOpen(),
                redisCandlestick.getClose(),
                redisCandlestick.getHigh(),
                redisCandlestick.getLow()
        );
    }
}
