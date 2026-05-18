package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyTypeModel;

import java.util.List;

public interface PropertyTypeService {
    List<PropertyTypeModel> findAll();
}
