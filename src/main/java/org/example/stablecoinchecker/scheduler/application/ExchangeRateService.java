package org.example.stablecoinchecker.scheduler.application;

import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.scheduler.domain.exchangeRate.ExchangeRate;
import org.example.stablecoinchecker.scheduler.domain.exchangeRate.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Transactional
    public ExchangeRate save(final ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }
}
