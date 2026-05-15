package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

@Builder
public record ResidenceResponse(
        Integer bedrooms,
        Integer bathrooms,
        String conservation,
        String orientation
) {
}

