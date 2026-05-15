package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

@Builder
public record PropertyExtraResponse(
        Integer id,
        String name
) {
}

