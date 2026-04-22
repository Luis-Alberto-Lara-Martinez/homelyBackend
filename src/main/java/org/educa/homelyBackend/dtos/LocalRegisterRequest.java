package org.educa.homelyBackend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LocalRegisterRequest(
        @NotBlank(message = "El name es requerido")
        String name,

        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "La password es requerida")
        String password,

        @NotBlank(message = "La confirmedPassword es requerida")
        String confirmedPassword
) {
}