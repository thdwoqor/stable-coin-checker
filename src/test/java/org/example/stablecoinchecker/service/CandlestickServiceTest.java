package org.example.stablecoinchecker.service;

import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.example.stablecoinchecker.chart.application.ActiveCandlestickGenerator;
import org.example.stablecoinchecker.chart.domain.Candlestick;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CandlestickRepository;
import org.example.stablecoinchecker.chart.domain.CryptoExchange;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchangePriceEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"app.scheduling.enable=false"})
class CandlestickServiceTest {

    @Autowired
    private ActiveCandlestickGenerator candlestickService;
    @Autowired
    private CandlestickRepository candlestickRepository;

    @Test
    @Disabled
    void 캔들스택을_생성할_수_있다() {
        //given
        List<CryptoExchangePriceEvent> events = List.of(
                new CryptoExchangePriceEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1100),
                        1731045875583L
                ),
                new CryptoExchangePriceEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1200),
                        1731045885583L
                ),
                new CryptoExchangePriceEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1000),
                        1731045895583L
                ),
                new CryptoExchangePriceEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1500),
                        1731045905583L
                ),
                new CryptoExchangePriceEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1400),
                        1731045905583L
                )
        );

        //when
        for (CryptoExchangePriceEvent event : events) {
            candlestickService.consumePriceEvent(event);
        }

        //then
        Candlestick candlestick1 = candlestickRepository.findById(
                CandlestickId.from(CryptoExchange.BITHUMB, "USDT", TimeInterval.MIN1, 1731045875583L)
        ).orElseThrow();
        Candlestick candlestick2 = candlestickRepository.findById(
                CandlestickId.from(CryptoExchange.BITHUMB, "USDT", TimeInterval.MIN1, 1731045905583L)
        ).orElseThrow();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(candlestick1.getOpen()).isEqualByComparingTo(new BigDecimal(1100));
            softly.assertThat(candlestick1.getClose()).isEqualByComparingTo(new BigDecimal(1000));
            softly.assertThat(candlestick1.getHigh()).isEqualByComparingTo(new BigDecimal(1200));
            softly.assertThat(candlestick1.getLow()).isEqualByComparingTo(new BigDecimal(1000));

            softly.assertThat(candlestick2.getOpen()).isEqualByComparingTo(new BigDecimal(1500));
            softly.assertThat(candlestick2.getClose()).isEqualByComparingTo(new BigDecimal(1400));
            softly.assertThat(candlestick2.getHigh()).isEqualByComparingTo(new BigDecimal(1500));
            softly.assertThat(candlestick2.getLow()).isEqualByComparingTo(new BigDecimal(1400));
        });
    }
}
