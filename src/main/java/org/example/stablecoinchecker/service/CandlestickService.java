package org.example.stablecoinchecker.service;

import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.infra.NamedLockWithJdbcTemplate;
import org.example.stablecoinchecker.infra.cex.CryptoExchangeTickerEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandlestickService {

    private static final int FIVE_SECONDS = 5;

    private final CandlestickGenerator CandlestickGenerator;
    private final NamedLockWithJdbcTemplate template;

    @EventListener
    @Async("candlestickGeneratorAsyncExecutor")
    public void candleStickGeneration(final CryptoExchangeTickerEvent event) {
        template.executeWithNamedLock(
                event.identifier() + event.symbol(),
                FIVE_SECONDS,
                () -> CandlestickGenerator.generateCandlesticks(event)
        );
    }

}
