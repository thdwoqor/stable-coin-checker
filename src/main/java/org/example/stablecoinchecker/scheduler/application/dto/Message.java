package org.example.stablecoinchecker.scheduler.application.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.Getter;
import org.example.stablecoinchecker.scheduler.domain.exchangeRate.ExchangeRate;
import org.example.stablecoinchecker.scheduler.infra.cex.StableCoin;

@Getter
public class Message {

    private final String message;

    private Message(final String message) {
        this.message = message;
    }

    public static Message create(
            final List<StableCoin> stableCoins,
            final ExchangeRate exchangeRate
    ) {
        StringBuffer sb = new StringBuffer();
        sb.append(formatExchangeRateMessage(exchangeRate));
        sb.append(formatStablecoinMessage(
                stableCoins,
                exchangeRate
        ));
        return new Message(sb.toString());
    }

    private static String formatExchangeRateMessage(final ExchangeRate exchangeRate) {
        StringBuilder sb = new StringBuilder();
        sb.append("• *환율*\n");
        sb.append("```복사\n");
        sb.append(String.format("USD/KRW : %,.2f원\n", exchangeRate.getPrice().doubleValue()));
        sb.append("```\n");
        return sb.toString();
    }

    private static String formatStablecoinMessage(
            final List<StableCoin> cryptoTicker,
            final ExchangeRate exchangeRate
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("• *국내 스테이블 코인 가격*\n");
        sb.append("```복사\n");
        for (StableCoin ticker : cryptoTicker) {
            sb.append(formatPriceMessage(ticker, exchangeRate));
        }
        sb.append("```\n");
        return sb.toString();
    }

    private static String formatPriceMessage(
            final StableCoin cryptoTicker,
            final ExchangeRate exchangeRate
    ) {
        return String.format(
                "%-8s(%s) : %,d원(%.1f%%)\n",
                cryptoTicker.getCex(),
                cryptoTicker.getSymbol(),
                cryptoTicker.getClose().intValue(),
                calculateKimchiPremium(cryptoTicker, exchangeRate)
        );
    }

    private static double calculateKimchiPremium(final StableCoin cryptoTicker, final ExchangeRate exchangeRate) {
        return cryptoTicker.getClose().divide(exchangeRate.getPrice(), 3, RoundingMode.HALF_DOWN)
                .subtract(BigDecimal.ONE)
                .multiply(new BigDecimal("100"))
                .setScale(1)
                .doubleValue();
    }
}
