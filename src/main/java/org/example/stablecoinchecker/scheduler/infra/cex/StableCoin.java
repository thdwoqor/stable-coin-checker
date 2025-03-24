package org.example.stablecoinchecker.scheduler.infra.cex;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class StableCoin {

    private final String cex;
    private final String symbol;
    private final BigDecimal close;
    private final Long timestamp;

    public StableCoin(final String cex, final String symbol, final BigDecimal close, final Long timestamp) {
        this.cex = cex;
        this.symbol = symbol;
        this.close = close;
        this.timestamp = timestamp;
    }
}
