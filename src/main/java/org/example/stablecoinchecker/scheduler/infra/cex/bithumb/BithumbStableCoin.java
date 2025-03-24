package org.example.stablecoinchecker.scheduler.infra.cex.bithumb;

import lombok.Getter;

@Getter
public enum BithumbStableCoin {
    USDT("USDT");

    private final String symbol;

    BithumbStableCoin(final String symbol) {
        this.symbol = symbol;
    }
}
