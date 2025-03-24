package org.example.stablecoinchecker.scheduler.infra.telegram;

public interface MessagingServiceProvider {

    void sendMessage(String message);
}
