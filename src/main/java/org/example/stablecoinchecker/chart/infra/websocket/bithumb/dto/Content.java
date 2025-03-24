package org.example.stablecoinchecker.chart.infra.websocket.bithumb.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Content {
    private String symbol;
    private String date;
    private String time;
    private BigDecimal closePrice;

    public Content(
            final String symbol,
            final String date,
            final String time,
            final BigDecimal closePrice
    ) {
        this.symbol = symbol;
        this.date = date;
        this.time = time;
        this.closePrice = closePrice;
    }

}
