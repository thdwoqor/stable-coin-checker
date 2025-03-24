package org.example.stablecoinchecker.scheduler.infra.cex.updit;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoinProvider;
import org.example.stablecoinchecker.scheduler.infra.cex.updit.dto.UpditTickerResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
@RequiredArgsConstructor
public class UpbitAdapter implements StableCoinProvider {

    private static final String PAYMENT_CURRENCY = "KRW";
    private final UpbitClient upbitClient;

    @Override
    public List<StableCoin> getStableCoins() {
        return Arrays.stream(UpbitStableCoin.values())
                .map(this::getStableCoinTicker)
                .toList();
    }

    private StableCoin getStableCoinTicker(final UpbitStableCoin stableCoin) {
        UpditTickerResponse response = upbitClient.getTicker(
                stableCoin.getSymbol(),
                PAYMENT_CURRENCY
        ).get(0);
        return response.toTickerResponse(CryptoExchange.UPBIT, stableCoin.getSymbol());
    }

}
