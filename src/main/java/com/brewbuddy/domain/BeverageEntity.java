package com.brewbuddy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "beverage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeverageEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private BeverageType type;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column(name = "brew_time_min_sec")
    private Integer brewTimeMinSec;

    @Column(name = "brew_time_max_sec")
    private Integer brewTimeMaxSec;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "beverage_tag",
            joinColumns = @JoinColumn(
                    name = "beverage_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "beverage_tag_beverage_id_fkey")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "beverage_tag_tag_id_fkey")
            )
    )
    @Builder.Default
    private Set<TagEntity> tags = new HashSet<>();

    @OneToOne(mappedBy = "beverage", cascade = ALL, orphanRemoval = true)
    private BeverageQuantityEntity beverageQuantity;

    @OneToMany(mappedBy = "beverage", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BrewLogEntity> brewLogs = new ArrayList<>();
}

