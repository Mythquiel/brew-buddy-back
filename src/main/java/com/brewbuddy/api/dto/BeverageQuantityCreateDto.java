package com.brewbuddy.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeverageQuantityCreateDto {

    @NotNull(message = "Beverage ID is required")
    private UUID beverageId;

    @PositiveOrZero(message = "Quantity must be zero or positive")
    @Builder.Default
    private Integer quantity = 0;
}
