package org.example.stablecoinchecker.scheduler.infra.cex.coinone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record Ticker(
        BigDecimal last,
        Long timestamp
) {
}
