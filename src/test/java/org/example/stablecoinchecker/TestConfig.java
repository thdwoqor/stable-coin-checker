package org.example.stablecoinchecker;

import javax.sql.DataSource;
import org.example.stablecoinchecker.chart.infra.NamedLockWithJdbcTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public NamedLockWithJdbcTemplate testNamedLockWithJdbcTemplate(DataSource dataSource) {
        return new NamedLockWithJdbcTemplate(dataSource) {
            @Override
            public void executeWithNamedLock(final String userLockName, final int timeoutSeconds,
                                             final Runnable runnable) {
                runnable.run();
            }
        };
    }
}
