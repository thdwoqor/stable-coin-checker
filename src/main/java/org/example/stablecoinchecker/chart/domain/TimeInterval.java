package org.example.stablecoinchecker.chart.domain;

import lombok.Getter;

@Getter
public enum TimeInterval {

    MIN1(60),
    MIN5(60 * 5),
    MIN15(60 * 15),
    MIN30(60 * 30),
    HOUR1(60 * 60),
    HOUR3(60 * 60 * 3),
    HOUR6(60 * 60 * 6),
    HOUR12(60 * 60 * 12),
    HOUR24(60 * 60 * 24),
    ;

    private int second;

    TimeInterval(final int second) {
        this.second = second;
    }

    public static long calculateTimestamp(final TimeInterval timeInterval, final Long timestamp) {
        long second = timeInterval.getSecond() * 1_000L;
        return timestamp - (timestamp % second);
    }

}
