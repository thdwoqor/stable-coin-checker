package org.example.stablecoinchecker.chart.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandlestickRepository extends JpaRepository<Candlestick, Long> {

    @Query(value = "SELECT * FROM candlestick c " +
            "WHERE c.crypto_exchange = :cryptoExchange " +
            "AND c.symbol = :symbol " +
            "AND c.time_interval = :timeInterval " +
            "AND c.timestamp < :timestamp " +
            "ORDER BY c.timestamp DESC LIMIT :limit",
            nativeQuery = true)
    List<Candlestick> findCandlesticks(
            @Param("cryptoExchange") String cryptoExchange,
            @Param("symbol") String symbol,
            @Param("timeInterval") String timeInterval,
            @Param("timestamp") Long timestamp,
            @Param("limit") int limit
    );
}
