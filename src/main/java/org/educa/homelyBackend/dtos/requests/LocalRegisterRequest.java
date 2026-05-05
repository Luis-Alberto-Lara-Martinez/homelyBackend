package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LocalRegisterRequest(
        @NotBlank(message = "El campo 'name' es requerido")
        String name,

        @NotBlank(message = "El campo 'email' es requerido")
        @Email(message = "Formato del campo 'email' inválido")
        String email,

        @NotBlank(message = "El campo 'password' es requerido")
        String password,

        @NotBlank(message = "El campo 'confirmedPassword' es requerido")
        String confirmedPassword
) {
}