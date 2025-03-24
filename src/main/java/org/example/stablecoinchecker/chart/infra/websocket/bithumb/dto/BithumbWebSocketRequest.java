package org.example.stablecoinchecker.chart.infra.websocket.bithumb.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BithumbWebSocketRequest {
    private String type;
    private List<String> symbols;
    private List<String> tickTypes;
}
