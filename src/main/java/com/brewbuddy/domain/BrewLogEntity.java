package com.brewbuddy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "brew_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrewLogEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "beverage_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "brew_log_beverage_id_fkey")
    )
    private BeverageEntity beverage;

    @Column(name = "amount_used", nullable = false)
    private int amountUsed;

    @Column(name = "brewed_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime brewedAt;
}
