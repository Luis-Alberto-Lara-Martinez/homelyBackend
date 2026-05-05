package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserPasswordRequest(
        @NotBlank(message = "El campo 'password' es requerido")
        String password,

        @NotBlank(message = "El campo 'confirmedPassword' es requerido")
        String confirmedPassword
) {
}