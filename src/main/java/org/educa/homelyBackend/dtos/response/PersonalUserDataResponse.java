package org.educa.homelyBackend.dtos.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PersonalUserDataResponse(
        @NotBlank(message = "El name es requerido")
        String name,

        @NotBlank(message = "La imagen es requerida")
        String imageUrl
) {
}