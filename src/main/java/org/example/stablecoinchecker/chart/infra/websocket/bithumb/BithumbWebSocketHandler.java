package org.example.stablecoinchecker.chart.infra.websocket.bithumb;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stablecoinchecker.scheduler.infra.cex.CryptoExchangePriceEvent;
import org.example.stablecoinchecker.scheduler.infra.cex.JsonUtils;
import org.example.stablecoinchecker.chart.infra.websocket.bithumb.dto.BithumbWebSocketRequest;
import org.example.stablecoinchecker.chart.infra.websocket.bithumb.dto.BithumbWebSocketResponse;
import org.example.stablecoinchecker.chart.infra.websocket.bithumb.dto.Content;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "application.runner",
        value = "enabled",
        havingValue = "true")
class BithumbWebSocketHandler extends TextWebSocketHandler {

    private static final int MILLISECOND = 1000;

    private WebSocketSession sessions;
    private final ApplicationEventPublisher publisher;
    private final JsonUtils jsonUtils;

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage(
                jsonUtils.serialize(
                        new BithumbWebSocketRequest(
                                "ticker",
                                List.of("USDT_KRW", "BTC_KRW"),
                                List.of("24H")
                        )
                )));
        sessions = session;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        jsonUtils.deserialize((String) message.getPayload(), BithumbWebSocketResponse.class)
                .ifPresent(this::dispatchTickerEvent);
    }

    private void dispatchTickerEvent(final BithumbWebSocketResponse response) {
        Content content = response.getContent();
        if (validate(content)) {
            publisher.publishEvent(
                    new CryptoExchangePriceEvent(
                            "BITHUMB",
                            content.getSymbol().split("_")[0],
                            content.getClosePrice(),
                            convertUnixTimestamp(content.getDate(), content.getTime())
                    ));
        }
    }

    private boolean validate(final Content content) {
        return content != null &&
                content.getSymbol() != null &&
                content.getClosePrice() != null &&
                content.getDate() != null &&
                content.getTime() != null;
    }

    private Long convertUnixTimestamp(final String date, final String time) {
        String dateTimeString = date + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return dateTime.toEpochSecond(zoneId.getRules().getOffset(dateTime)) * MILLISECOND;
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
            sessions = null;
            log.error("빗썸 웹소켓과 연결이 끊어졌습니다.", e);
        }
    }

    public boolean isNotConnected() {
        return sessions == null;
    }
}
