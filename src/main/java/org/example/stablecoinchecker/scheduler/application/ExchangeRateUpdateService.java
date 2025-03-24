package org.example.stablecoinchecker.scheduler.application;

import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.scheduler.domain.exchangeRate.ExchangeRate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateUpdateService {

    private final ExchangeRateRequester exchangeRateRequester;
    private final ExchangeRateService exchangeRateService;

    public ExchangeRate updateExchangeRate() {
        ExchangeRate exchangeRate = exchangeRateRequester.getCurrentExchangeRate();
        return exchangeRateService.save(exchangeRate);
    }
}
