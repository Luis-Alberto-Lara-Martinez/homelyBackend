package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record PropertyTypeDto(
        Integer id,
        String name
) {
}
