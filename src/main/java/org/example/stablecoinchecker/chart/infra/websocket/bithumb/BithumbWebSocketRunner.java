package org.example.stablecoinchecker.chart.infra.websocket.bithumb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "application.runner",
        value = "enabled",
        havingValue = "true")
public class BithumbWebSocketRunner {

    private final BithumbWebSocketHandler handler;
    private final WebSocketClient client;

    public void connect() {
        client.execute(handler, "wss://pubwss.bithumb.com/pub/ws");
    }

    @Scheduled(fixedRate = 2000)
    public void reconnect() {
        if (handler.isNotConnected()) {
            connect();
        }
    }

}
