package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record FindAllUsersRequest(
        @NotNull(message = "El campo 'page' no puede ser nulo") Integer page,
        @NotNull(message = "El campo 'size no puede ser nulo'") Integer size,
        String sortBy
) {
}
