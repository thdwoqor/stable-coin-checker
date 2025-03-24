package org.example.stablecoinchecker.chart.infra.websocket.upbit.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpbitWebSocketRequest {
    private String type;
    private List<String> codes;
}
