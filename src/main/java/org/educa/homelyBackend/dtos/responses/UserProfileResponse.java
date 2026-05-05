package org.educa.homelyBackend.dtos.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserProfileResponse(
        @NotBlank(message = "El email no puede ser null o estar vacío")
        String name,

        @NotBlank(message = "La URL de la imagen no puede ser null o estar vacía")
        String imageUrl
) {
}
