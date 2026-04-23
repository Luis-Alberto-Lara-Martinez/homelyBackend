package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserNameRequest(
        @NotBlank(message = "El campo 'name' es requerido")
        String name
) {
}