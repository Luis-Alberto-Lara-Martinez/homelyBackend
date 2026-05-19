package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record PropertyImageDto(
        Integer id,
        String imageUrl,
        Integer displayOrder
) {
}
