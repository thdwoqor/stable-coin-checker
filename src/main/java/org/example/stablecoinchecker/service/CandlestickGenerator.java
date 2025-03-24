package org.example.stablecoinchecker.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.domain.candlestick.CandlestickId;
import org.example.stablecoinchecker.domain.candlestick.CryptoExchange;
import org.example.stablecoinchecker.domain.candlestick.RedisCandlestick;
import org.example.stablecoinchecker.domain.candlestick.RedisCandlestickRepository;
import org.example.stablecoinchecker.domain.candlestick.TimeInterval;
import org.example.stablecoinchecker.infra.cex.CryptoExchangeTickerEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandlestickGenerator {

    private final RedisCandlestickRepository redisCandlestickRepository;

    public void generateCandlesticks(final CryptoExchangeTickerEvent event) {
        for (TimeInterval timeInterval : TimeInterval.values()) {
            CandlestickId candlestickId = toCandlestickId(event, timeInterval);
            Optional<RedisCandlestick> findRedisCandlestick = redisCandlestickRepository.findById(candlestickId.serialized());

            if(findRedisCandlestick.isPresent()){
                RedisCandlestick redisCandlestick = findRedisCandlestick.get();
                redisCandlestick.update(event.price());
                redisCandlestickRepository.save(redisCandlestick);
            }else{
                redisCandlestickRepository.save(RedisCandlestick.createNew(toCandlestickId(event, timeInterval), event.price()));
            }
        }
    }

    private CandlestickId toCandlestickId(final CryptoExchangeTickerEvent event, final TimeInterval timeInterval) {
        return CandlestickId.from(
                CryptoExchange.from(event.identifier()),
                event.symbol(),
                timeInterval,
                event.timestamp()
        );
    }

}
