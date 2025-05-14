package org.example.stablecoinchecker.scheduler.infra.cex;

import java.math.BigDecimal;


public record CryptoExchangePriceEvent(
        String cryptoExchange,
        String symbol,
        BigDecimal price,
        Long timestamp
) {
}
