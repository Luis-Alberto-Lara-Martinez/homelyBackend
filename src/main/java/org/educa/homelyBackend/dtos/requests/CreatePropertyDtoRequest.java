package org.educa.homelyBackend.dtos.requests;

import org.educa.homelyBackend.dtos.EnergyCertificateDto;
import org.educa.homelyBackend.dtos.PropertyAddressDto;
import org.educa.homelyBackend.dtos.PropertyExtraDto;
import org.educa.homelyBackend.dtos.ResidenceDto;

import java.math.BigDecimal;
import java.util.List;

public record CreatePropertyDtoRequest(
        String type,
        String status,
        String transaction,
        String title,
        String description,
        Integer surface,
        BigDecimal price,
        List<CreatePropertyImageDtoRequest> images,
        List<PropertyExtraDto> extras,
        PropertyAddressDto address,
        ResidenceDto residence,
        EnergyCertificateDto energyCertificate
) {
}
