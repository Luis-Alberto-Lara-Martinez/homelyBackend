package org.educa.homelyBackend.dtos;

import lombok.Builder;

@Builder
public record EnergyCertificateDto(
        Boolean hasCertificate,
        String consumptionScale,
        Integer consumptionValue,
        String emissionsScale,
        Integer emissionsValue
) {
}
