package dev.pedroscheurer.nambadelivery.delivery.tracking.api.model.dtos;

import jakarta.validation.constraints.NotBlank;

public record ContactPointInput(
        @NotBlank
        String zipCode,

        @NotBlank
        String street,

        @NotBlank
        String number,

        String complement,

        @NotBlank
        String name,

        @NotBlank
        String phone
) {
}
