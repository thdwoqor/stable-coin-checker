package org.example.stablecoinchecker.chart.ui;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.chart.application.CandlestickService;
import org.example.stablecoinchecker.chart.application.dto.CandlestickResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChartController {

    private final CandlestickService candlestickService;

    @GetMapping("/klines")
    public ResponseEntity<List<CandlestickResponse>> getKlineData(
            @RequestParam(defaultValue = "UPBIT") String cryptoExchange,
            @RequestParam(defaultValue = "USDT") String symbol,
            @RequestParam(defaultValue = "MIN1") String interval,
            @RequestParam(defaultValue = "-1") long endTime,
            @RequestParam(defaultValue = "500") int limit
    ) {
        if (endTime == -1) {
            endTime = Instant.now().toEpochMilli();
        }
        return ResponseEntity.ok(candlestickService.getKlineData(
                cryptoExchange, symbol, interval, endTime, limit
        ));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(
    ) {
        return ResponseEntity.ok("test");
    }
}
