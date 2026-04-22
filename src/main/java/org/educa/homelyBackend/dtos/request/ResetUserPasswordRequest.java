package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record ResetUserPasswordRequest(
        @NotBlank(message = "El token es requerido")
        String token,

        @NotBlank(message = "La password es requerido")
        String password,

        @NotBlank(message = "La confirmedPassword es requerida")
        String confirmedPassword
) {
}