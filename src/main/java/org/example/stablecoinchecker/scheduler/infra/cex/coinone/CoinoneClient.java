package org.example.stablecoinchecker.scheduler.infra.cex.coinone;

import org.example.stablecoinchecker.scheduler.infra.cex.coinone.dto.CoinoneTickerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Coinone", url = "https://api.coinone.co.kr/public")
public interface CoinoneClient {
    @GetMapping("/v2/ticker_new/{currency}/{symbol}")
    CoinoneTickerResponse getTicker(
            @RequestParam("symbol") String symbol,
            @RequestParam("currency") String currency
    );
}
