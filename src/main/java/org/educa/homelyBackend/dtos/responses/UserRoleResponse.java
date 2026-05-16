package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

@Builder
public record UserRoleResponse(
        Integer id,
        String name
) {
}
