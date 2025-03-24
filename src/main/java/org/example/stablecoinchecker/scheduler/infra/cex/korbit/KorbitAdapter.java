package org.example.stablecoinchecker.scheduler.infra.cex.korbit;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoinProvider;
import org.example.stablecoinchecker.scheduler.infra.cex.korbit.dto.KorbitTickerResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(4)
@RequiredArgsConstructor
public class KorbitAdapter implements StableCoinProvider {

    private static final String PAYMENT_CURRENCY = "krw";

    private final KorbitClient korbitClient;

    @Override
    public List<StableCoin> getStableCoins() {
        return Arrays.stream(KorbitStableCoin.values())
                .map(this::getStableCoinTicker)
                .toList();
    }

    private StableCoin getStableCoinTicker(final KorbitStableCoin stableCoin) {
        KorbitTickerResponse response = korbitClient.getTicker(
                stableCoin.getSymbol(),
                PAYMENT_CURRENCY
        );
        return response.toTickerResponse(CryptoExchange.KORBIT, stableCoin.getSymbol());
    }

}
