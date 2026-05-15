package org.educa.homelyBackend.facades.api;

import org.educa.homelyBackend.dtos.responses.PropertyResponse;

import java.util.List;

public interface PropertyApiFacade {

    List<PropertyResponse> findAllPropertiesWithDetails();
}
