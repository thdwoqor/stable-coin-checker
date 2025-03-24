package org.example.stablecoinchecker.scheduler.application;

import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.scheduler.infra.telegram.MessagingServiceProvider;
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
