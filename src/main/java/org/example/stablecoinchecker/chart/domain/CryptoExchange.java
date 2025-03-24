package org.example.stablecoinchecker.chart.domain;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public enum CryptoExchange {
    UPBIT,
    BITHUMB,
    COINONE,
    GOPAX,
    KORBIT;

    public static CryptoExchange from(final String name) {
        return Stream.of(CryptoExchange.values())
                .filter(category -> category.toString().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 이름의 거래소를 찾을 수 없습니다: " + name));
    }
}
