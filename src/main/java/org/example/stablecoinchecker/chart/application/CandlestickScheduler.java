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

    /*
        비활성 상태의 캔들을 영구 저장하는 스케줄러
        예: 5분봉 차트를 생성할 때, 현재 시간이 1시 11분이라면 1시 10분까지의 캔들은 이미 완성된 상태입니다.
        따라서 이들은 '비활성 캔들'로 간주되어 영구 저장 대상이 됩니다.
     */
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
