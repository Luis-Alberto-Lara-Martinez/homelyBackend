package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyStatusModel;

import java.util.List;

public interface PropertyStatusService {
    PropertyStatusModel findByNameOrThrow(String name);

    List<PropertyStatusModel> findAll();
}
