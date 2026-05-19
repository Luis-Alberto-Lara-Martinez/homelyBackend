package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record ResidenceDto(
        Integer bedrooms,
        Integer bathrooms,
        String conservation,
        String orientation
) {
}
