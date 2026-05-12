package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyTypeModel;

public interface PropertyTypeService {
    PropertyTypeModel findByName(String name);
}
