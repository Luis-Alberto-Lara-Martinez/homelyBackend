package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyAddressModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PropertyAddressService {
    List<PropertyAddressModel> findAddressesWithinRadius(
            double latitude,
            double longitude,
            Integer radiusKm
    );

    Page<PropertyAddressModel> findAll(Integer pageNumber, Integer pageSize);

    PropertyAddressModel save(PropertyAddressModel propertyAddressModel);
}
