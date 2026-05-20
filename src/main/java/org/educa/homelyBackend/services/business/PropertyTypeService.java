package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyTypeModel;

import java.util.List;

public interface PropertyTypeService {
    PropertyTypeModel findByNameOrThrow(String name);

    PropertyTypeModel findById(Integer id);

    List<PropertyTypeModel> findAll();
}
