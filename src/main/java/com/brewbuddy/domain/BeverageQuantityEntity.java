package com.brewbuddy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "beverage_quantity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeverageQuantityEntity {

    @Id
    @Column(name = "beverage_id", nullable = false)
    private UUID beverageId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(
            name = "beverage_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "beverage_quantity_beverage_id_fkey")
    )
    private BeverageEntity beverage;

    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Integer quantity = 0;
}
