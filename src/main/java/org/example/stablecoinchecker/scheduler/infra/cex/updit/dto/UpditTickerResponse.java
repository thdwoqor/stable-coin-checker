package org.example.stablecoinchecker.scheduler.infra.cex.updit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;

public record UpditTickerResponse(
        @JsonProperty("trade_price")
        BigDecimal tradePrice,
        Long timestamp
) {
    public StableCoin toTickerResponse(final CryptoExchange cryptoExchange, final String orderCurrency) {
        return new StableCoin(
                cryptoExchange.name(),
                orderCurrency.toUpperCase(),
                tradePrice,
                timestamp
        );
    }
}
