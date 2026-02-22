package com.brewbuddy.api.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeverageQuantityUpdateDto {

    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;
}
