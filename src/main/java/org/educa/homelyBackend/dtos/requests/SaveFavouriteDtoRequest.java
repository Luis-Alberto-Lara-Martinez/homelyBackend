package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record SaveFavouriteDtoRequest(
        @NotNull(message = "Property ID no puede ser nulo")
        Integer propertyId
) {
}
