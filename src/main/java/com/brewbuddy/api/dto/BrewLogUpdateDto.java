package com.brewbuddy.api.dto;

import jakarta.validation.constraints.Positive;
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
public class BrewLogUpdateDto {

    @Positive(message = "Amount used must be positive")
    private Integer amountUsed;
    private OffsetDateTime brewedAt;
    private UUID userId;
}
