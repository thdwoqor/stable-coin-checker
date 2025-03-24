package org.example.stablecoinchecker.global.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {

    STABLE_COIN("stablecoin", ConstantConfig.DEFAULT_TTL_SEC, ConstantConfig.DEFAULT_MAX_SIZE);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

    class ConstantConfig {
        static final int DEFAULT_TTL_SEC = 900;
        static final int DEFAULT_MAX_SIZE = 100;
    }
}
