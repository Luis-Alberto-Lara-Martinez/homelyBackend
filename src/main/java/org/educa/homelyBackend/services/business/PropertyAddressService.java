package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyAddressModel;

public interface PropertyAddressService {
    PropertyAddressModel findByPropertyId(Integer propertyId);

    PropertyAddressModel save(PropertyAddressModel propertyAddressModel);

    void delete(PropertyAddressModel propertyAddressModel);
}
