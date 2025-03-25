package org.example.stablecoinchecker.chart.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestick;
import org.example.stablecoinchecker.chart.domain.ActiveCandlestickRepository;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CryptoExchange;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.example.stablecoinchecker.chart.infra.NamedLockWithJdbcTemplate;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchangePriceEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveCandlestickGenerator {

    private static final int FIVE_SECONDS = 5;

    private final NamedLockWithJdbcTemplate template;
    private final ActiveCandlestickRepository activeCandlestickRepository;

    @EventListener
    @Async("candlestickGeneratorAsyncExecutor")
    public void consumePriceEvent(final CryptoExchangePriceEvent event) {
        template.executeWithNamedLock(
                event.identifier() + event.symbol(),
                FIVE_SECONDS,
                () -> generateActiveCandlesticks(event)
        );
    }

    private void generateActiveCandlesticks(final CryptoExchangePriceEvent event) {
        for (TimeInterval timeInterval : TimeInterval.values()) {
            CandlestickId candlestickId = toCandlestickId(event, timeInterval);
            Optional<ActiveCandlestick> findRedisCandlestick = activeCandlestickRepository.findById(
                    candlestickId.serialized());

            if (findRedisCandlestick.isPresent()) {
                ActiveCandlestick activeCandlestick = findRedisCandlestick.get();
                activeCandlestick.update(event.price());
                activeCandlestickRepository.save(activeCandlestick);
            } else {
                activeCandlestickRepository.save(
                        ActiveCandlestick.create(toCandlestickId(event, timeInterval), event.price())
                );
            }
        }
    }

    private CandlestickId toCandlestickId(final CryptoExchangePriceEvent event, final TimeInterval timeInterval) {
        return CandlestickId.from(
                CryptoExchange.from(event.identifier()),
                event.symbol(),
                timeInterval,
                event.timestamp()
        );
    }

}
