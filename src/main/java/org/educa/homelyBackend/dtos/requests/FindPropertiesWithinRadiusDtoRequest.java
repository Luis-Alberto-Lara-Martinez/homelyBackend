package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FindPropertiesWithinRadiusDtoRequest(
        @NotNull(message = "El campo 'latitude' no puede estar vacío ni ser null")
        BigDecimal latitude,

        @NotNull(message = "El campo 'longitude' no puede estar vacío ni ser null")
        BigDecimal longitude,

        @NotNull(message = "El campo 'radiusKm' no puede estar vacío ni ser null")
        Integer radiusKm
) {
}
