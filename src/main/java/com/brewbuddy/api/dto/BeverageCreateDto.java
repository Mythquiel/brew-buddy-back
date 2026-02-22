package com.brewbuddy.api.dto;

import com.brewbuddy.domain.BeverageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeverageCreateDto {

    @NotNull(message = "Type is required")
    private BeverageType type;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 255, message = "Brand must not exceed 255 characters")
    private String brand;

    @Positive(message = "Minimum brew time must be positive")
    private Integer brewTimeMinSec;

    @Positive(message = "Maximum brew time must be positive")
    private Integer brewTimeMaxSec;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    // Optional: IDs of existing tags to associate with this beverage
    private Set<UUID> tagIds;

    // Optional: Initial quantity (creates BeverageQuantityEntity)
    private Integer initialQuantity;
}
