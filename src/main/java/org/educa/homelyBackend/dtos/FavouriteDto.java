package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record FavouriteDto(
        Integer id,
        PropertyDto property
) {
}
