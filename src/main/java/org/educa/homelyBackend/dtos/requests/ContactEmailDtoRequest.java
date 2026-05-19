package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactEmailDtoRequest(
        @NotBlank(message = "El campo 'name' no puede estar vacío")
        String name,

        @NotBlank(message = "El campo 'email' no puede estar vacío")
        @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
        String email,

        @NotBlank(message = "El campo 'message' no puede estar vacío")
        String message
) {
}
