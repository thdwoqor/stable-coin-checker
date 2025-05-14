package org.example.stablecoinchecker.chart.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class Identifier {

    @Enumerated(value = EnumType.STRING)
    private CryptoExchange cryptoExchange;
    private String symbol;
    @Enumerated(value = EnumType.STRING)
    private TimeInterval timeInterval;
    private Long timestamp;

    private Identifier(
            final CryptoExchange cryptoExchange,
            final String symbol,
            final TimeInterval timeInterval,
            final Long timestamp
    ) {
        this.cryptoExchange = cryptoExchange;
        this.symbol = symbol;
        this.timeInterval = timeInterval;
        this.timestamp = timestamp;
    }

    public static Identifier from(
            final CryptoExchange cryptoExchange,
            final String symbol,
            final TimeInterval timeInterval,
            final Long timestamp
    ) {
        return new Identifier(
                cryptoExchange,
                symbol,
                timeInterval,
                TimeInterval.calculateTimestamp(timeInterval, timestamp)
        );
    }

    public boolean isSameAs(
            final CryptoExchange cryptoExchange,
            final String symbol,
            final TimeInterval timeInterval
    ) {
        return this.cryptoExchange.equals(cryptoExchange) &&
                this.symbol.equals(symbol) &&
                this.timeInterval.equals(timeInterval);
    }

}
