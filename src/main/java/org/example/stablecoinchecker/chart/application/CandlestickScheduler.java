package org.example.stablecoinchecker.chart.application;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestickRepository;
import org.example.stablecoinchecker.chart.domain.Candlestick;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CandlestickRepository;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestick;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandlestickScheduler {

    private final ActiveCandlestickRepository activeCandlestickRepository;
    private final CandlestickRepository candlestickRepository;

    @Scheduled(cron = "3 */5 * * * *")
    public void batchCandleData() {
        List<ActiveCandlestick> activeCandlesticks = activeCandlestickRepository.findAll();

        for (ActiveCandlestick activeCandlestick : activeCandlesticks) {
            CandlestickId candlestickId = CandlestickId.deserialized(activeCandlestick.getId());
            if (candlestickId.getTimestamp() < Instant.now().toEpochMilli()) {
                candlestickRepository.save(toCandlestick(activeCandlestick, candlestickId));
                activeCandlestickRepository.deleteById(activeCandlestick.getId());
            }
        }
    }

    private Candlestick toCandlestick(
            final ActiveCandlestick activeCandlestick,
            final CandlestickId candlestickId
    ) {
        return Candlestick.create(
                candlestickId,
                activeCandlestick.getOpen(),
                activeCandlestick.getClose(),
                activeCandlestick.getHigh(),
                activeCandlestick.getLow()
        );
    }
}
