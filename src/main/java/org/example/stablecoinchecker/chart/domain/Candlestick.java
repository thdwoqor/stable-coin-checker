package org.example.stablecoinchecker.chart.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "candlestick")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Candlestick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Identifier identifier;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;

    private Candlestick(
            final Identifier identifier,
            final BigDecimal open,
            final BigDecimal close,
            final BigDecimal high,
            final BigDecimal low
    ) {
        this.identifier = identifier;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    public static Candlestick create(
            final Identifier identifier,
            final BigDecimal open
    ) {
        return new Candlestick(identifier, open, open, open, open);
    }

    public static Candlestick create(
            final Identifier identifier,
            final Set<BigDecimal> prices
    ) {
        Iterator<BigDecimal> it = prices.iterator();
        BigDecimal first = it.next();
        Candlestick newCandlestick = Candlestick.create(identifier, first);

        while (it.hasNext()) {
            newCandlestick.update(it.next());
        }

        return newCandlestick;
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
