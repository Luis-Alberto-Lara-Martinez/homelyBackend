package org.educa.homelyBackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetUserPasswordRequest(
        @NotBlank(message = "El token es requerido")
        String token,

        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "La password es requerido")
        String password,

        @NotBlank(message = "La confirmedPassword es requerida")
        String confirmedPassword
) {
}