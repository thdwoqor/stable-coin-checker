package org.example.stablecoinchecker.scheduler.infra.cex.korbit;

import org.example.stablecoinchecker.scheduler.infra.cex.korbit.dto.KorbitTickerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Korbit", url = "https://api.korbit.co.kr")
public interface KorbitClient {
    @GetMapping("/v1/ticker/detailed?currency_pair={symbol}_{currency}")
    KorbitTickerResponse getTicker(
            @RequestParam("symbol") String symbol,
            @RequestParam("currency") String currency
    );
}
