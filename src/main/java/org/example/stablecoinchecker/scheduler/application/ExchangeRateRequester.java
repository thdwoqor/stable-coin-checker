package org.example.stablecoinchecker.scheduler.application;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.global.config.Resilience4jConfig;
import org.example.stablecoinchecker.scheduler.domain.exchangeRate.ExchangeRate;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.coincodex.CoinCodexExchangeRateClient;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.coincodex.dto.CoinCodexExchangeRateResponse;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.manana.MananaExchangeRateClient;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.manana.dto.MananaExchangeRateResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateRequester {
    private static final String USD_KRW = "KRW=X";
    private static final int FIFTEEN_MINUTES = 900;

    private final CoinCodexExchangeRateClient coinCodexExchangeRateClient;
    private final MananaExchangeRateClient mananaExchangeRateClient;

    @CircuitBreaker(name = Resilience4jConfig.CB_EXCHANGE_RATE)
    @Retry(name = Resilience4jConfig.RETRY_EXCHANGE_RATE, fallbackMethod = "fallback")
    public ExchangeRate getCurrentExchangeRate() {
        List<MananaExchangeRateResponse> responses = mananaExchangeRateClient.getExchangeRate();

        MananaExchangeRateResponse result = responses.stream()
                .filter(response -> response.symbol().equals(USD_KRW))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("원달러 환율 정보를 불러오는 데 실패했습니다."));

        if (Instant.now().getEpochSecond() - result.timeStamp() > FIFTEEN_MINUTES) {
            throw new IllegalArgumentException("원달러 환율 정보를 불러오는 데 실패했습니다.");
        }

        return new ExchangeRate(result.price());
    }

    public ExchangeRate fallback(final Exception e) {
        CoinCodexExchangeRateResponse response = coinCodexExchangeRateClient.getExchangeRate();

        return new ExchangeRate(response.fiatRates().krw());
    }
}
