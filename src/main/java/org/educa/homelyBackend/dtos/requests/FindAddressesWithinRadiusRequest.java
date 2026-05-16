package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record FindAddressesWithinRadiusRequest(
        @NotNull(message = "El campo 'latitude' no puede estar vacío ni ser null")
        Double latitude,

        @NotNull(message = "El campo 'longitude' no puede estar vacío ni ser null")
        Double longitude,

        @NotNull(message = "El campo 'radiusKm' no puede estar vacío ni ser null")
        Integer radiusKm
) {
}
