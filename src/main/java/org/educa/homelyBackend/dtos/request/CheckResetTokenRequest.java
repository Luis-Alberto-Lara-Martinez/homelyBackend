package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CheckResetTokenRequest(
        @NotBlank(message = "El token es requerido")
        String token
) {
}