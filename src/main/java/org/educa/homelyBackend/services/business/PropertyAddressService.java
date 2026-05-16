package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyAddressModel;

import java.util.List;

public interface PropertyAddressService {
    List<PropertyAddressModel> findAddressesWithinRadius(
            double latitude,
            double longitude,
            Integer radiusKm
    );

    PropertyAddressModel save(PropertyAddressModel propertyAddressModel);
}
