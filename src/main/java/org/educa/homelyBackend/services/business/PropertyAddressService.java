package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyAddressModel;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyAddressService {
    List<PropertyAddressModel> findByLatitudeAndLongitudeWithinRadius(
            BigDecimal latitude,
            BigDecimal longitude,
            Integer radiusKm
    );

    PropertyAddressModel save(PropertyAddressModel propertyAddressModel);

    PropertyAddressModel update(Integer propertyId, PropertyAddressModel propertyAddressModel);
}
