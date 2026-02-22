package com.brewbuddy.api.dto;

import com.brewbuddy.domain.BeverageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeverageDto {
    private UUID id;
    private BeverageType type;
    private String name;
    private String brand;
    private Integer brewTimeMinSec;
    private Integer brewTimeMaxSec;
    private String imageUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
