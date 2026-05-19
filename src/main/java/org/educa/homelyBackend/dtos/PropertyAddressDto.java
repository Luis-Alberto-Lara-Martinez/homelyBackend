package org.educa.homelyBackend.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PropertyAddressDto(
        String street,
        String number,
        String floor,
        String door,
        String postalCode,
        String city,
        String province,
        String country,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
