package org.example.stablecoinchecker.infra.cex;

import java.util.List;

public interface StableCoinProvider {

    List<StableCoin> getStableCoins();
}
