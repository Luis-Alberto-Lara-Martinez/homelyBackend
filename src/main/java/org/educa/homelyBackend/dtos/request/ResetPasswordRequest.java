package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "El campo 'token' es requerido")
        String token,

        @NotBlank(message = "El campo 'password' es requerido")
        String password,

        @NotBlank(message = "El campo 'confirmedPassword' es requerido")
        String confirmedPassword
) {
}