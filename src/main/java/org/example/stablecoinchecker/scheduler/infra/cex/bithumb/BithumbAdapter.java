package org.example.stablecoinchecker.scheduler.infra.cex.bithumb;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoinProvider;
import org.example.stablecoinchecker.scheduler.infra.cex.bithumb.dto.BithumbTickerResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
@RequiredArgsConstructor
public class BithumbAdapter implements StableCoinProvider {

    private static final String PAYMENT_CURRENCY = "KRW";

    private final BithumbClient bithumbClient;

    @Override
    public List<StableCoin> getStableCoins() {
        return Arrays.stream(BithumbStableCoin.values())
                .map(this::getStableCoinTicker)
                .toList();
    }

    private StableCoin getStableCoinTicker(final BithumbStableCoin stableCoin) {
        BithumbTickerResponse response = bithumbClient.getTicker(
                stableCoin.getSymbol(),
                PAYMENT_CURRENCY
        );
        return response.toTickerResponse(CryptoExchange.BITHUMB, stableCoin.getSymbol());
    }

}
