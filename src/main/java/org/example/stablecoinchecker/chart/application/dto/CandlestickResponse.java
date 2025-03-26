package org.example.stablecoinchecker.chart.application.dto;

import java.math.BigDecimal;
import org.example.stablecoinchecker.chart.domain.Candlestick;

public record CandlestickResponse(
        BigDecimal open,
        BigDecimal close,
        BigDecimal high,
        BigDecimal low,
        Long time
) {

    public static CandlestickResponse from(final Candlestick candlestick) {
        return new CandlestickResponse(
                candlestick.getOpen().setScale(2),
                candlestick.getClose().setScale(2),
                candlestick.getHigh().setScale(2),
                candlestick.getLow().setScale(2),
                candlestick.getCandlestickId().getTimestamp()
        );
    }
}
