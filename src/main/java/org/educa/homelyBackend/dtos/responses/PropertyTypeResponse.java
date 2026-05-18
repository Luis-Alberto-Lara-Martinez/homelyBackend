package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

@Builder
public record PropertyTypeResponse(
        Integer id,
        String name
) {
}
