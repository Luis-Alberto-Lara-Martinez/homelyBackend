package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserNameRequest(
        @NotBlank(message = "El campo 'name' es requerido")
        String name
) {
}