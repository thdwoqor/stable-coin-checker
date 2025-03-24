package org.example.stablecoinchecker.scheduler.infra.cex.bithumb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record Data(

        @JsonProperty("closing_price")
        BigDecimal closingPrice,
        Long date
) {
}
