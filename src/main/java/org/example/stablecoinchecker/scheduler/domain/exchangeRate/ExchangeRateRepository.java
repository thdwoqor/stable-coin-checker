package org.example.stablecoinchecker.scheduler.domain.exchangeRate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
