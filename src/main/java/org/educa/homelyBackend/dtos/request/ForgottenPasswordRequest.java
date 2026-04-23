package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgottenPasswordRequest(
        @NotBlank(message = "El campo 'email' es requerido")
        @Email(message = "Formato del campo 'email' inválido")
        String email
) {
}