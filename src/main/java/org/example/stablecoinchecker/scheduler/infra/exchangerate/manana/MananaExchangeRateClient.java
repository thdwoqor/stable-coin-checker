package org.example.stablecoinchecker.scheduler.infra.exchangerate.manana;

import java.util.List;
import org.example.stablecoinchecker.scheduler.infra.exchangerate.manana.dto.MananaExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "Manana", url = "https://api.manana.kr")
public interface MananaExchangeRateClient {
    @GetMapping("/exchange.json")
    List<MananaExchangeRateResponse> getExchangeRate();
}
