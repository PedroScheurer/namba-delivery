package dev.pedroscheurer.nambadelivery.delivery.tracking.api.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemInput(
        @NotBlank
        String name,
        @NotNull
        @Min(1)
        Integer quantity
) {
}
