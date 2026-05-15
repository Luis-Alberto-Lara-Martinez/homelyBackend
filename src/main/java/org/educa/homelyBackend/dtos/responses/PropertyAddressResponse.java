package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PropertyAddressResponse(
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

