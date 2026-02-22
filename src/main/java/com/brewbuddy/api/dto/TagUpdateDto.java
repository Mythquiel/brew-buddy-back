package com.brewbuddy.api.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagUpdateDto {

    @Size(max = 100, message = "Tag name must not exceed 100 characters")
    private String name;
}
