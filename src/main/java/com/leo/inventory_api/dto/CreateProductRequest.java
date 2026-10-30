package com.leo.inventory_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateProductRequest(@NotBlank(message = "Name is required") String name,
                                   @NotNull(message = "Stock is required") @Min(value = 0, message = "Stock cannot be negative") Integer stock,
                                   @NotNull(message = "Price is required") @Positive(message = "Price must be greater than zero") BigDecimal price) {
}
