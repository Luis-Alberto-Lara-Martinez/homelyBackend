package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record PropertyTransactionDto(
        Integer id,
        String name
) {
}
