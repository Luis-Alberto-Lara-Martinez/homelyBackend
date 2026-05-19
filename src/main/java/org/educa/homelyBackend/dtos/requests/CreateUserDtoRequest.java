package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDtoRequest(
        @NotBlank(message = "El campo 'role' no puede estar vacío")
        String role,

        @NotBlank(message = "El campo 'status' no puede estar vacío")
        String status,

        @NotBlank(message = "El campo 'name' no puede estar vacío")
        String name,

        @NotBlank(message = "El campo 'email' no puede estar vacío")
        String email,

        @NotBlank(message = "El campo 'password' no puede estar vacío")
        String password,

        @NotBlank(message = "El campo 'confirmedPassword' no puede estar vacío")
        String confirmedPassword
) {
}
