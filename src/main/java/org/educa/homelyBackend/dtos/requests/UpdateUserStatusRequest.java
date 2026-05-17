package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserStatusRequest(
        @NotBlank(message = "El campo 'email' no puede estar vacío")
        @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
        String email,

        @NotBlank(message = "El campo 'status' no puede estar vacío")
        String status
) {
}
