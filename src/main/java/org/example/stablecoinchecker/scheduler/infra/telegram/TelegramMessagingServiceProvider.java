package org.example.stablecoinchecker.scheduler.infra.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(4)
@RequiredArgsConstructor
public class TelegramMessagingServiceProvider implements MessagingServiceProvider {

    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.chatId}")
    private String chatId;
    private final TelegramClient telegramClient;

    @Override
    public void sendMessage(final String message) {
        if (token == null || chatId == null) {
            throw new IllegalArgumentException("텔레그램 작업을 수행하기 위해 필요한 토큰 및 채팅 ID가 설정되지 않았습니다.");
        }
        telegramClient.sendMessage(token, chatId, message, "MarkdownV2");
    }
}
