package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LocalLogInRequest(
        @NotBlank(message = "El campo 'email' es requerido")
        @Email(message = "Formato del campo 'email' inválido")
        String email,

        @NotBlank(message = "El campo 'password' es requerido")
        String password
) {
}