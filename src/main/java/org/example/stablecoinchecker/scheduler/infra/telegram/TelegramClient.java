package org.example.stablecoinchecker.scheduler.infra.telegram;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Telegram", url = "https://api.telegram.org")
public interface TelegramClient {

    @PostMapping(value = "/bot{token}/sendMessage")
    void sendMessage(
            @PathVariable("token") String token,
            @RequestParam("chat_id") String chatId,
            @RequestParam("text") String text,
            @RequestParam("parse_mode") String parseMode
    );
}
