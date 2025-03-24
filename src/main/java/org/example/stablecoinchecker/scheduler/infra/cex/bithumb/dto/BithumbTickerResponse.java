package org.example.stablecoinchecker.scheduler.infra.cex.bithumb.dto;

import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;

public record BithumbTickerResponse(
        String status,
        Data data
) {

    public StableCoin toTickerResponse(final CryptoExchange cryptoExchange, final String orderCurrency) {
        return new StableCoin(
                cryptoExchange.name(),
                orderCurrency.toUpperCase(),
                data.closingPrice(),
                data.date()
        );
    }
}
