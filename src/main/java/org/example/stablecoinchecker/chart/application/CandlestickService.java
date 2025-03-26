package org.example.stablecoinchecker.chart.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.application.dto.CandlestickResponse;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestick;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestickRepository;
import org.example.stablecoinchecker.chart.domain.Candlestick;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CandlestickRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandlestickService {

    private final CandlestickRepository candlestickRepository;
    private final ActiveCandlestickRepository activeCandlestickRepository;

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

        List<Candlestick> activeCandlestick = findActiveCandlestick(cryptoExchange, symbol, interval);

        return Stream.of(candlesticks, activeCandlestick)
                .flatMap(List::stream)
                .map(CandlestickResponse::from)
                .sorted((c1, c2) -> Long.compare(c2.time(), c1.time()))
                .toList();
    }

    private List<Candlestick> findActiveCandlestick(
            final String cryptoExchange,
            final String symbol,
            final String interval
    ) {
        List<Candlestick> candlesticks = new ArrayList<>();
        List<ActiveCandlestick> activeCandlesticks = activeCandlestickRepository.findAll();
        for (ActiveCandlestick activeCandlestick : activeCandlesticks) {
            CandlestickId candlestickId = CandlestickId.deserialized(activeCandlestick.getId());
            if (candlestickId.isSameChart(cryptoExchange, symbol, interval)) {
                candlesticks.add(Candlestick.create(
                        candlestickId,
                        activeCandlestick.getOpen(),
                        activeCandlestick.getClose(),
                        activeCandlestick.getHigh(),
                        activeCandlestick.getLow()
                ));
            }
        }
        return candlesticks;
    }
}
