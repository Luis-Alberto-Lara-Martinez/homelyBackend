package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record PropertyExtraDto(
        Integer id,
        String name
) {
}
