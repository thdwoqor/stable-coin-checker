package org.example.stablecoinchecker.domain;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class StableCoinRepositoryTest {

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    void 생성시간에서_초단위_제거() {
        //given
        StableCoin stableCoin = new StableCoin(
                new BigDecimal(1400),
                "UPBIT",
                "USDT",
                new Ticker(
                        new BigDecimal(1371),
                        new BigDecimal(1412),
                        new BigDecimal(1370),
                        new BigDecimal(1414),
                        BigDecimal.ONE
                )
        );

        //when
        em.persist(stableCoin);

        //then
        Assertions.assertThat(stableCoin.getCreatedAt() % 60).isZero();
    }
}