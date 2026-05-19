package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyModel;
import org.springframework.data.domain.Page;

public interface PropertyService {
    PropertyModel findByIdOrThrow(Integer id);

    Page<PropertyModel> findAll(Integer page, Integer size);

    PropertyModel save(PropertyModel propertyModel);

    void delete(PropertyModel property);
}
