package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyStatusModel;

public interface PropertyStatusService {
    PropertyStatusModel findByName(String name);

    PropertyStatusModel save(PropertyStatusModel propertyStatusModel);

    void delete(PropertyStatusModel propertyStatusModel);
}
