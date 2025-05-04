package org.example.stablecoinchecker.chart.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "candlestick2")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Candlestick {

    @EmbeddedId
    private CandlestickId candlestickId;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;

    private Candlestick(
            final CandlestickId candlestickId,
            final BigDecimal open,
            final BigDecimal close,
            final BigDecimal high,
            final BigDecimal low
    ) {
        this.candlestickId = candlestickId;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    public static Candlestick create(
            final CandlestickId candlestickId,
            final BigDecimal open
    ) {
        return new Candlestick(candlestickId, open, open, open, open);
    }

    public static Candlestick create(
            final CandlestickId candlestickId,
            final Set<BigDecimal> prices
    ) {
        Iterator<BigDecimal> it = prices.iterator();
        BigDecimal first = it.next();
        Candlestick cs = Candlestick.create(candlestickId, first);

        while (it.hasNext()) {
            cs.update(it.next());
        }

        return cs;
    }

    public void update(final BigDecimal price) {
        this.close = price;
        if (price.compareTo(low) < 0) {
            this.low = price;
        }
        if (price.compareTo(high) > 0) {
            this.high = price;
        }
    }
}
