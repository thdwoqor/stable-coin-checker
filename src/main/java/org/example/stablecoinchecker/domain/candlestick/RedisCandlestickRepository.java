package org.example.stablecoinchecker.domain.candlestick;

import org.springframework.data.repository.ListCrudRepository;

public interface RedisCandlestickRepository extends ListCrudRepository<RedisCandlestick, String> {
}
