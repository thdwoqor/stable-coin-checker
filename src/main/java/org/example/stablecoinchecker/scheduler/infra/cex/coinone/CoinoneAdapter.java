package org.example.stablecoinchecker.scheduler.infra.cex.coinone;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoinProvider;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;
import org.example.stablecoinchecker.scheduler.infra.cex.coinone.dto.CoinoneTickerResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order(3)
@RequiredArgsConstructor
public class CoinoneAdapter implements StableCoinProvider {

    private static final String PAYMENT_CURRENCY = "KRW";

    private final CoinoneClient coinoneClient;

    @Override
    public List<StableCoin> getStableCoins() {
        return Arrays.stream(CoinoneStableCoin.values())
                .map(this::getStableCoinTicker)
                .toList();
    }

    private StableCoin getStableCoinTicker(final CoinoneStableCoin stableCoin) {
        CoinoneTickerResponse response = coinoneClient.getTicker(
                stableCoin.getSymbol(),
                PAYMENT_CURRENCY
        );
        return response.toTickerResponse(CryptoExchange.COINONE, stableCoin.getSymbol());
    }

}
