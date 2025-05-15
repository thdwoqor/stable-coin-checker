package org.example.stablecoinchecker.chart.infra.websocket.upbit;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchangePriceEvent;
import org.example.stablecoinchecker.scheduler.infra.cex.JsonUtils;
import org.example.stablecoinchecker.scheduler.infra.cex.updit.dto.UpbitTicketRequest;
import org.example.stablecoinchecker.chart.infra.websocket.upbit.dto.UpbitWebSocketRequest;
import org.example.stablecoinchecker.chart.infra.websocket.upbit.dto.UpbitWebSocketResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "application.runner",
        value = "enabled",
        havingValue = "true")
public class UpbitWebSocketHandler extends BinaryWebSocketHandler {

    private final ApplicationEventPublisher publisher;
    private final JsonUtils jsonUtils;
    private WebSocketSession sessions;

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage(jsonUtils.serialize(
                List.of(
                        new UpbitTicketRequest(UUID.randomUUID().toString()),
                        new UpbitWebSocketRequest("ticker", List.of("KRW-USDT", "KRW-BTC"))
                )
        )));
        sessions = session;
    }

    @Override
    public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message) {
        jsonUtils.deserialize((ByteBuffer) message.getPayload(), UpbitWebSocketResponse.class)
                .ifPresent(this::dispatchTickerEvent);
    }

    private void dispatchTickerEvent(final UpbitWebSocketResponse response) {
        if (validate(response)) {
            publisher.publishEvent(
                    new CryptoExchangePriceEvent(
                            "UPBIT",
                            response.getCode().split("-")[1],
                            response.getTradePrice(),
                            response.getTimestamp()
                    ));
        }
    }

    private boolean validate(final UpbitWebSocketResponse response) {
        return response != null &&
                response.getCode() != null &&
                response.getTimestamp() != null &&
                response.getTradePrice() != null;
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        sessions = null;
    }

    @Scheduled(fixedRate = 2000, initialDelay = 2000)
    public void expire() {
        try {
            sessions.sendMessage(new TextMessage("PING"));
        } catch (Exception e) {
            log.error("업비트 웹소켓과 연결이 끊어졌습니다.", e);
            sessions = null;
        }
    }

    public boolean isNotConnected() {
        return sessions == null;
    }
}
