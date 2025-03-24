package org.example.stablecoinchecker.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.coincodex.CoinCodexExchangeRateClient;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.coincodex.dto.CoinCodexExchangeRateResponse;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.coincodex.dto.FiatRates;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.manana.MananaExchangeRateClient;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.manana.dto.MananaExchangeRateResponse;
import org.example.stablecoinchecker.scheduler.application.ExchangeRateRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ExchangeRateRequesterTest {

    @MockBean
    private MananaExchangeRateClient mananaExchangeRateClient;
    @MockBean
    private CoinCodexExchangeRateClient coinCodexExchangeRateClient;
    @Autowired
    private ExchangeRateRequester exchangeRateRequester;
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    void setUp() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("CB_EXCHANGE_RATE");
        circuitBreaker.transitionToClosedState();
    }

    @Test
    void 환율_정보_조회_예외_발생시_재시도_테스트() {
        //given-when
        BigDecimal result = new BigDecimal("1383.25");
        when(mananaExchangeRateClient.getExchangeRate())
                .thenReturn(List.of(new MananaExchangeRateResponse("KRW=X", new BigDecimal("1382.39"), 1721000000L)));
        when(coinCodexExchangeRateClient.getExchangeRate())
                .thenReturn(new CoinCodexExchangeRateResponse(new FiatRates(result)));
        BigDecimal exchangeRate = exchangeRateRequester.getCurrentExchangeRate().getPrice();

        //then
        verify(mananaExchangeRateClient, times(2)).getExchangeRate();
        verify(coinCodexExchangeRateClient, times(1)).getExchangeRate();
        Assertions.assertThat(exchangeRate).isEqualTo(result);
    }

    @Test
    void 환율_정보_조회_예외_발생시_서킷_브레이커_OPEN_테스트() {
        //given-when
        BigDecimal result = new BigDecimal("1383.25");
        when(mananaExchangeRateClient.getExchangeRate())
                .thenReturn(List.of(new MananaExchangeRateResponse("KRW=X", new BigDecimal("1382.39"), 1721000000L)));
        when(coinCodexExchangeRateClient.getExchangeRate())
                .thenReturn(new CoinCodexExchangeRateResponse(new FiatRates(result)));

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("CB_EXCHANGE_RATE");
        State initialState = circuitBreaker.getState();

        for (int i = 0; i < 5; i++) {
            exchangeRateRequester.getCurrentExchangeRate();
        }

        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(State.OPEN);
        Assertions.assertThat(circuitBreaker.getState()).isNotEqualTo(initialState);
    }

    @Test
    void 서킷_브레이커_HALF_OPEN_상태에서_CLOSE_전환_테스트() {
        //given-when
        BigDecimal result = new BigDecimal("1383.25");
        when(mananaExchangeRateClient.getExchangeRate())
                .thenReturn(List.of(new MananaExchangeRateResponse("KRW=X", new BigDecimal("1382.39"),
                        Instant.now().getEpochSecond())));
        when(coinCodexExchangeRateClient.getExchangeRate())
                .thenReturn(new CoinCodexExchangeRateResponse(new FiatRates(result)));

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("CB_EXCHANGE_RATE");
        circuitBreaker.transitionToOpenState();
        circuitBreaker.transitionToHalfOpenState();
        State initialState = circuitBreaker.getState();

        for (int i = 0; i < 2; i++) {
            exchangeRateRequester.getCurrentExchangeRate();
        }

        Assertions.assertThat(initialState).isEqualTo(State.HALF_OPEN);
        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(State.CLOSED);
    }

    @Test
    void 서킷_브레이커_OPEN_상태에서는_Fallback_Method_를_반환한다() {
        //given-when
        BigDecimal result = new BigDecimal("1383.25");
        when(mananaExchangeRateClient.getExchangeRate())
                .thenReturn(List.of(new MananaExchangeRateResponse("KRW=X", new BigDecimal("1382.39"),
                        Instant.now().getEpochSecond())));
        when(coinCodexExchangeRateClient.getExchangeRate())
                .thenReturn(new CoinCodexExchangeRateResponse(new FiatRates(result)));

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("CB_EXCHANGE_RATE");
        circuitBreaker.transitionToOpenState();

        for (int i = 0; i < 5; i++) {
            exchangeRateRequester.getCurrentExchangeRate();
        }

        Assertions.assertThat(circuitBreaker.getState()).isEqualTo(State.OPEN);
        verify(mananaExchangeRateClient, times(0)).getExchangeRate();
        verify(coinCodexExchangeRateClient, times(5)).getExchangeRate();
    }
}
