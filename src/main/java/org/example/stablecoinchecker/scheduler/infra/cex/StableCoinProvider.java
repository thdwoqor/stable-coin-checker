package org.example.stablecoinchecker.scheduler.infra.cex;

import java.util.List;

public interface StableCoinProvider {

    List<StableCoin> getStableCoins();
}
