package org.example.stablecoinchecker.chart.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timestamp {

    @Enumerated(value = EnumType.STRING)
    private TimeInterval timeInterval;
    private Long timestamp;

    public Timestamp(final TimeInterval timeInterval, final Long timestamp) {
        this.timeInterval = timeInterval;
        this.timestamp = TimeInterval.calculateTimestamp(timeInterval, timestamp);
    }

}
