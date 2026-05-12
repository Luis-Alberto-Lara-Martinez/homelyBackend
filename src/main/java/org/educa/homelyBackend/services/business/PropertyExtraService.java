package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyExtraModel;

public interface PropertyExtraService {
    PropertyExtraModel findByName(String name);

    PropertyExtraModel save(PropertyExtraModel propertyExtraModel);

    void delete(PropertyExtraModel propertyExtraModel);
}
