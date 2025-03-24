package org.example.stablecoinchecker.scheduler.infra.cex.updit;

import java.util.List;
import org.example.stablecoinchecker.scheduler.infra.cex.updit.dto.UpditTickerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Updit", url = "https://api.upbit.com")
public interface UpbitClient {
    @GetMapping(value = "/v1/ticker?markets={currency}-{symbol}")
    List<UpditTickerResponse> getTicker(
            @RequestParam("symbol") String symbol,
            @RequestParam("currency") String currency
    );
}
