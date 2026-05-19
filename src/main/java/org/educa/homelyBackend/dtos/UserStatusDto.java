package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record UserStatusDto(
        Integer id,
        String name
) {
}
