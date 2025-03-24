package org.example.stablecoinchecker.chart.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandlestickRepository extends JpaRepository<Candlestick, CandlestickId> {
}
