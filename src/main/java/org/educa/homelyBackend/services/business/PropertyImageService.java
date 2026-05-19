package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyImageModel;
import org.educa.homelyBackend.models.PropertyModel;

import java.util.List;

public interface PropertyImageService {
    List<PropertyImageModel> findAllByPropertyId(PropertyModel property);

    PropertyImageModel save(PropertyImageModel propertyImageModel);

    void delete(PropertyImageModel propertyImageModel);
}
