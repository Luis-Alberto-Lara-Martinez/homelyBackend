package org.educa.homelyBackend.facades.api;

import org.educa.homelyBackend.dtos.responses.PropertyTypeResponse;

import java.util.List;

public interface PropertyTypeApiFacade {
    List<PropertyTypeResponse> findAll();
}
