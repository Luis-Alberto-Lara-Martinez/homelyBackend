package org.educa.homelyBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserPasswordRequest(
        @NotBlank(message = "El password es requerido")
        String password,

        @NotBlank(message = "El confirmed password es requerido")
        String confirmedPassword
) {
}