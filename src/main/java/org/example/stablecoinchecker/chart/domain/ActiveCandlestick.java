package org.example.stablecoinchecker.chart.domain;

import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;


@Getter
@RedisHash(value = "candlestick", timeToLive = 604800)
public class ActiveCandlestick {

    @Id
    private String id;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;

    private ActiveCandlestick(
            final String id,
            final BigDecimal open,
            final BigDecimal close,
            final BigDecimal high,
            final BigDecimal low
    ) {
        this.id = id;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    public static ActiveCandlestick create(
            final CandlestickId candlestickId,
            final BigDecimal price
    ) {
        return new ActiveCandlestick(
                candlestickId.serialized(),
                price,
                price,
                price,
                price
        );
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
