package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyImageModel;

import java.util.List;

public interface PropertyImageService {
    List<PropertyImageModel> findAllByPropertyId(Integer propertyId);

    PropertyImageModel save(PropertyImageModel propertyImageModel);

    void delete(PropertyImageModel propertyImageModel);
}
