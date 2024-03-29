package org.example.stablecoinchecker.infra.cex.bithumb.dto;

import org.example.stablecoinchecker.infra.cex.CryptocurrencyExchange;
import org.example.stablecoinchecker.infra.cex.StableCoinTicker;
import org.example.stablecoinchecker.infra.cex.bithumb.BithumbStableCoin;

public record BithumbTickerResponse(
        String status,
        ExchangeData data
) {

    public StableCoinTicker toStableCoinTicker(BithumbStableCoin symbol) {
        return new StableCoinTicker(
                CryptocurrencyExchange.BITHUMB,
                symbol.getName().toUpperCase(),
                data.closingPrice()
        );
    }
}
