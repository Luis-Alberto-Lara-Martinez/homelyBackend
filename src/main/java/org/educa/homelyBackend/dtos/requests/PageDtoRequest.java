package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record PageDtoRequest(
        @NotNull(message = "El campo 'page' es requerido")
        Integer page,

        @NotNull(message = "El campo 'size' es requerido")
        Integer size
) {
}
