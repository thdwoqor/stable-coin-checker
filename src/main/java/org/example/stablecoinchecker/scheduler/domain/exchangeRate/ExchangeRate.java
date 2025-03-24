package org.example.stablecoinchecker.scheduler.domain.exchangeRate;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stablecoinchecker.global.BaseEntity;

@Getter
@Entity
@Table(name = "exchange_rate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRate extends BaseEntity {

    private BigDecimal price;
    private Long createdAt;

    public ExchangeRate(final BigDecimal price) {
        this.price = price;
    }

    @PrePersist
    private void setCreatedAt() {
        createdAt = Instant.now().truncatedTo(ChronoUnit.MINUTES).toEpochMilli();
    }
}
