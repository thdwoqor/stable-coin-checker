package org.example.stablecoinchecker.chart.infra.websocket.bithumb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BithumbWebSocketResponse {

    private Content content;

    public BithumbWebSocketResponse(
            final Content content
    ) {
        this.content = content;
    }
}
