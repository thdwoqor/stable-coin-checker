package org.example.stablecoinchecker.scheduler.infra.exchangerate.manana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record MananaExchangeRateResponse(
        String symbol,
        BigDecimal price,
        @JsonProperty("timestamp")
        Long timeStamp
) {
}
