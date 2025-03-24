package org.example.stablecoinchecker.scheduler.infra.cex.gopax;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoinProvider;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;
import org.example.stablecoinchecker.scheduler.infra.cex.gopax.dto.GopaxTickerResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Order(5)
@Service
@RequiredArgsConstructor
public class GopaxAdapter implements StableCoinProvider {

    private static final String PAYMENT_CURRENCY = "KRW";

    private final GopaxClient gopaxClient;

    @Override
    public List<StableCoin> getStableCoins() {
        return Arrays.stream(GopaxStableCoin.values())
                .map(this::getStableCoinTicker)
                .toList();
    }

    private StableCoin getStableCoinTicker(final GopaxStableCoin stableCoin) {
        GopaxTickerResponse response = gopaxClient.getTicker(
                stableCoin.getSymbol(),
                PAYMENT_CURRENCY
        );
        return response.toTickerResponse(CryptoExchange.GOPAX, stableCoin.getSymbol());
    }

}
