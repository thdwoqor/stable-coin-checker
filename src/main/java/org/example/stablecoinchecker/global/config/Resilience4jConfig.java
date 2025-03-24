package org.example.stablecoinchecker.global.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Resilience4jConfig {

    public static final String CB_EXCHANGE_RATE = "CB_EXCHANGE_RATE";
    public static final String RETRY_EXCHANGE_RATE = "RETRY_EXCHANGE_RATE";
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;

    @Bean
    public CircuitBreaker customCircuitBreaker() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)   // 실패 50% 이상 서킷 오픈
                .slowCallDurationThreshold(Duration.ofMillis(5000))    // 5000ms 이상 소요 시 실패로 간주
                .slowCallRateThreshold(50)  // slowCallDurationThreshold 초과 비율이 50% 이상 시 서킷 오픈
                .waitDurationInOpenState(Duration.ofHours(1)) // open -> half open 전환 전 대기 시간
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .minimumNumberOfCalls(10)    // 서킷브레이커가 값을 집계하기 전, 최소 호출해야 하는 횟수.
                .slidingWindowSize(10)   // 슬라이딩 윈도우의 크기. 해당 값은 서킷브레이커가 CLOSED인 경우 값을 집계할 때 사용합니다.
                .permittedNumberOfCallsInHalfOpenState(2) // HALF_OPEN일 때, 지정한 횟수만큼 성공 시 Closed 상태로 변경
                .build();

        return circuitBreakerRegistry.circuitBreaker(CB_EXCHANGE_RATE, circuitBreakerConfig);
    }

    @Bean
    public Retry customRetry() {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(1000))
                .build();

        return retryRegistry.retry(RETRY_EXCHANGE_RATE, retryConfig);
    }
}
