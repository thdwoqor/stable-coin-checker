package org.example.stablecoinchecker.infra.cex.bithumb;

import lombok.Getter;

@Getter
public enum BithumbStableCoin {
    USDT("USDT");

    private final String name;

    BithumbStableCoin(final String name) {
        this.name = name;
    }
}
