package org.example.stablecoinchecker.scheduler.infra.cex.korbit;

import lombok.Getter;

@Getter
public enum KorbitStableCoin {
    USDT("usdt");

    private final String symbol;

    KorbitStableCoin(final String symbol) {
        this.symbol = symbol;
    }
}
