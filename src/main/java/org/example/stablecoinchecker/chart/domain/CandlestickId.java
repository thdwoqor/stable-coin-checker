package org.example.stablecoinchecker.chart.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class CandlestickId implements Serializable {

    @Enumerated(value = EnumType.STRING)
    private CryptoExchange cryptoExchange;
    private String symbol;
    @Enumerated(value = EnumType.STRING)
    private TimeInterval timeInterval;
    private Long timestamp;

    private CandlestickId(
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

    public static CandlestickId from(
            final CryptoExchange cryptoExchange,
            final String symbol,
            final TimeInterval timeInterval,
            final Long timestamp
    ) {
        return new CandlestickId(
                cryptoExchange,
                symbol,
                timeInterval,
                TimeInterval.calculateTimestamp(timeInterval, timestamp)
        );
    }

    public String serialized() {
        return String.join(",", cryptoExchange.name(), symbol, timeInterval.name(), timestamp.toString());
    }

    public static CandlestickId deserialized(final String id) {
        String[] split = id.split(",");
        return new CandlestickId(
                CryptoExchange.valueOf(split[0]),
                split[1],
                TimeInterval.valueOf(split[2]),
                Long.parseLong(split[3])
        );
    }
}
