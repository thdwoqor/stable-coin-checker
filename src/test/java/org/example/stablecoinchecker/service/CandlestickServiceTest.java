package org.example.stablecoinchecker.service;

import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.example.stablecoinchecker.TestConfig;
import org.example.stablecoinchecker.chart.application.CandlestickService;
import org.example.stablecoinchecker.chart.domain.Candlestick;
import org.example.stablecoinchecker.chart.domain.CandlestickId;
import org.example.stablecoinchecker.chart.domain.CandlestickRepository;
import org.example.stablecoinchecker.chart.domain.CryptoExchange;
import org.example.stablecoinchecker.chart.domain.TimeInterval;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchangeTickerEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource(properties = {"app.scheduling.enable=false"})
class CandlestickServiceTest {

    @Autowired
    private CandlestickService candlestickService;
    @Autowired
    private CandlestickRepository candlestickRepository;

    @Test
    @Disabled
    void 캔들스택을_생성할_수_있다() {
        //given
        List<CryptoExchangeTickerEvent> events = List.of(
                new CryptoExchangeTickerEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1100),
                        1731045875583L
                ),
                new CryptoExchangeTickerEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1200),
                        1731045885583L
                ),
                new CryptoExchangeTickerEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1000),
                        1731045895583L
                ),
                new CryptoExchangeTickerEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1500),
                        1731045905583L
                ),
                new CryptoExchangeTickerEvent(
                        "BITHUMB",
                        "USDT",
                        new BigDecimal(1400),
                        1731045905583L
                )
        );

        //when
        for (CryptoExchangeTickerEvent event : events) {
            candlestickService.candleStickGeneration(event);
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
