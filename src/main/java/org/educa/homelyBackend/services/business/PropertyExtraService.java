package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyExtraModel;
import org.educa.homelyBackend.models.PropertyTypeModel;

import java.util.List;

public interface PropertyExtraService {
    PropertyExtraModel findByNameOrThrow(String name);

    List<PropertyExtraModel> findByPropertyType(PropertyTypeModel propertyTypeModel);

    PropertyExtraModel save(PropertyExtraModel propertyExtraModel);
}
