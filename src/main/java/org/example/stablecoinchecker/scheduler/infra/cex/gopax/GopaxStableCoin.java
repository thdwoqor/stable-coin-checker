package org.example.stablecoinchecker.scheduler.infra.cex.gopax;

import lombok.Getter;

@Getter
public enum GopaxStableCoin {
    USDT("USDT");

    private final String symbol;

    GopaxStableCoin(final String symbol) {
        this.symbol = symbol;
    }
}
