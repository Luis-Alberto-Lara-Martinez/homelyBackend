package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GeneratePropertyDescriptionByAIDtoRequest(
        @NotBlank(message = "El campo 'type' no puede estar vacío")
        String type,

        @NotBlank(message = "El campo 'transaction' no puede estar vacío")
        String transaction,

        @NotBlank(message = "El campo 'address' no puede estar vacío")
        String address,

        @NotBlank(message = "El campo 'city' no puede estar vacío")
        String city,

        @NotNull(message = "El campo 'surface' no puede ser null")
        Integer surface,

        Integer bedrooms,

        Integer bathrooms,

        @NotNull(message = "El campo 'extras' no puede ser null")
        List<@NotBlank(message = "Los extras no pueden estar vacíos") String> extras
) {
}

