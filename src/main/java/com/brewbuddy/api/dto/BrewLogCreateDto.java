package com.brewbuddy.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrewLogCreateDto {

    @NotNull(message = "Beverage ID is required")
    private UUID beverageId;

    @Positive(message = "Amount used must be positive")
    private int amountUsed;

    private UUID userId;
}
