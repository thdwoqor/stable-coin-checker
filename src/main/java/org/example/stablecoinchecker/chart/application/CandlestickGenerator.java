package org.example.stablecoinchecker.chart.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestickRepository;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CryptoExchange;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestick;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchangeTickerEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandlestickGenerator {

    private final ActiveCandlestickRepository activeCandlestickRepository;

    public void generateCandlesticks(final CryptoExchangeTickerEvent event) {
        for (TimeInterval timeInterval : TimeInterval.values()) {
            CandlestickId candlestickId = toCandlestickId(event, timeInterval);
            Optional<ActiveCandlestick> findRedisCandlestick = activeCandlestickRepository.findById(candlestickId.serialized());

            if(findRedisCandlestick.isPresent()){
                ActiveCandlestick activeCandlestick = findRedisCandlestick.get();
                activeCandlestick.update(event.price());
                activeCandlestickRepository.save(activeCandlestick);
            }else{
                activeCandlestickRepository.save(ActiveCandlestick.create(toCandlestickId(event, timeInterval), event.price()));
            }
        }
    }

    private CandlestickId toCandlestickId(final CryptoExchangeTickerEvent event, final TimeInterval timeInterval) {
        return CandlestickId.from(
                CryptoExchange.from(event.identifier()),
                event.symbol(),
                timeInterval,
                event.timestamp()
        );
    }

}
