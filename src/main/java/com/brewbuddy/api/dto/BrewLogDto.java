package com.brewbuddy.api.dto;

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
public class BrewLogDto {
    private UUID id;
    private UUID beverageId;
    private int amountUsed;
    private OffsetDateTime brewedAt;
    private UUID userId;
}
