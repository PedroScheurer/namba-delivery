package dev.pedroscheurer.nambadelivery.courier.management.api.model.dtos;

import jakarta.validation.constraints.NotBlank;

public record CourierInput(@NotBlank String name,
                           @NotBlank String phone) {
}
