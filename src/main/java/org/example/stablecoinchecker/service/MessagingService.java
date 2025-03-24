package org.example.stablecoinchecker.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.example.stablecoinchecker.infra.cex.StableCoin;
import org.example.stablecoinchecker.domain.exchangeRate.ExchangeRate;
import org.example.stablecoinchecker.infra.telegram.MessagingServiceProvider;
import org.example.stablecoinchecker.service.dto.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final MessagingServiceProvider messagingServiceProvider;
    private final ExchangeRateUpdateService exchangeRateUpdateService;
    private final StableCoinRequester stableCoinRequester;

//    @Scheduled(cron = "${schedule.cron}")
//    @SchedulerLock(
//            name = "scheduledSendMessageTask",
//            lockAtLeastFor = "4m",
//            lockAtMostFor = "4m"
//    )
//    public void sendMessage() {
//        ExchangeRate exchangeRate = exchangeRateUpdateService.updateExchangeRate();
//        List<StableCoin> stableCoins = stableCoinRequester.getStableCoins();
//
//        Message message = Message.create(stableCoins, exchangeRate);
//
//        messagingServiceProvider.sendMessage(message.getMessage());
//    }

}
