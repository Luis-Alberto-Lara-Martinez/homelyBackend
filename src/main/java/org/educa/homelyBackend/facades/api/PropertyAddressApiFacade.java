package org.educa.homelyBackend.facades.api;

import org.educa.homelyBackend.dtos.responses.PropertyResponse;

import java.util.List;

public interface PropertyAddressApiFacade {
    List<PropertyResponse> findAddressesWithinRadius(
            double latitude,
            double longitude,
            Integer radiusKm
    );
}
