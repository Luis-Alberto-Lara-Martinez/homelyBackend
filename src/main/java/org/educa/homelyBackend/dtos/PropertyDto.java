package org.educa.homelyBackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record PropertyDto(
        Integer id,
        String type,
        String status,
        String transaction,
        String title,
        String description,
        Integer surface,
        BigDecimal price,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Madrid")
        Instant createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Madrid")
        Instant updatedAt,

        String updatedBy,
        List<PropertyImageDto> images,
        List<PropertyExtraDto> extras,
        PropertyAddressDto address,
        ResidenceDto residence,
        EnergyCertificateDto energyCertificate
) {
}
