package org.example.stablecoinchecker.scheduler.infra.cex.korbit.dto;

import java.math.BigDecimal;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;

public record KorbitTickerResponse(
        BigDecimal last,
        Long timestamp
) {
    public StableCoin toTickerResponse(final CryptoExchange cryptoExchange, final String orderCurrency) {
        return new StableCoin(
                cryptoExchange.name(),
                orderCurrency.toUpperCase(),
                last,
                timestamp
        );
    }
}
