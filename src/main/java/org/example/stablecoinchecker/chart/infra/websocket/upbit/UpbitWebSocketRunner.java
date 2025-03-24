package org.example.stablecoinchecker.chart.infra.websocket.upbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

//https://memo-the-day.tistory.com/74
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "application.runner",
        value = "enabled",
        havingValue = "true")
public class UpbitWebSocketRunner {

    private final UpbitWebSocketHandler handler;
    private final WebSocketClient client;

    public void connect() {
        client.execute(handler, "wss://api.upbit.com/websocket/v1");
    }

    @Scheduled(fixedRate = 2000)
    public void reconnect() {
        if (handler.isNotConnected()) {
            connect();
        }
    }

}
