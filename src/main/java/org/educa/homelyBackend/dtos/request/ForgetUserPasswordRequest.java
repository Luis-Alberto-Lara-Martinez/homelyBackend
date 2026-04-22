package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgetUserPasswordRequest(
        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        String email
) {
}