package org.example.stablecoinchecker.scheduler.infra.exchangerate.coincodex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record FiatRates(
        @JsonProperty("KRW")
        BigDecimal krw
) {
}
