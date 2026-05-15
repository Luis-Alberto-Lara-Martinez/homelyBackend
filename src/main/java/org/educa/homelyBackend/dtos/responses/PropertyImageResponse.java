package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

@Builder
public record PropertyImageResponse(
        Integer id,
        String imageUrl,
        Integer displayOrder
) {
}

