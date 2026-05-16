package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record DeleteUserRequest(
        @NotNull(message = "El campo 'id' no puede ser nulo")
        Integer id
) {
}
