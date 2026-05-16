package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

@Builder
public record UserStatusResponse(
        Integer id,
        String name
) {
}
