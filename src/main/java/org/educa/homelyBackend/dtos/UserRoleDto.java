package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record UserRoleDto(
        Integer id,
        String name
) {
}
