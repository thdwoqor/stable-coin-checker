package org.example.stablecoinchecker.domain.candlestick;

import org.assertj.core.api.Assertions;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.example.stablecoinchecker.chart.domain.Timestamp;
import org.junit.jupiter.api.Test;

class TimestampTest {

    @Test
    void 현재_시간을_캔들_차트_Timestamp로_변환할_수_있다() {
        Timestamp timestamp = new Timestamp(TimeInterval.MIN1, 1731045875583L);
        Assertions.assertThat(timestamp.getTimestamp()).isEqualTo(1731045900000L);
    }

}
