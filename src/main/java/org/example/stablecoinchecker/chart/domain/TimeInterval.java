package org.example.stablecoinchecker.chart.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum TimeInterval {

    MIN1(60),
    MIN5(60 * 5),
    MIN15(60 * 15),
    MIN30(60 * 30),
    ;

    private int second;

    TimeInterval(final int second) {
        this.second = second;
    }

    public static long calculateTimestamp(final TimeInterval timeInterval, final Long timestamp) {
        int second = timeInterval.getSecond();
        return Instant.ofEpochMilli(timestamp - (timestamp % (second * 1000L)) + (second * 1000L))
                .truncatedTo(ChronoUnit.MINUTES).toEpochMilli();
    }

}
