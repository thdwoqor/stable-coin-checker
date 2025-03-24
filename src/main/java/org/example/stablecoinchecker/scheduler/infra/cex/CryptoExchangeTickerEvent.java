package org.example.stablecoinchecker.scheduler.infra.cex;

import java.math.BigDecimal;


public record CryptoExchangeTickerEvent(
        String identifier,
        String symbol,
        BigDecimal price,
        Long timestamp
) {
}
