package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserNameRequest(
        @NotBlank(message = "El name es requerido")
        String name
) {
}