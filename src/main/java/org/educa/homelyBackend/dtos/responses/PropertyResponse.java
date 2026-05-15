package org.educa.homelyBackend.dtos.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PropertyResponse(
        Integer id,
        String user,
        String type,
        String status,
        String transaction,
        String title,
        String description,
        Integer surface,
        BigDecimal price,
        List<PropertyImageResponse> images,
        List<PropertyExtraResponse> extras,
        PropertyAddressResponse address,
        ResidenceResponse residence
) {
}
