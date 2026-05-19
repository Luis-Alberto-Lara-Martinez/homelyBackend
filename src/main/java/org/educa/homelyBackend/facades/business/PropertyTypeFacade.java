package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.PropertyTypeDto;

import java.util.List;

public interface PropertyTypeFacade {
    List<PropertyTypeDto> findAll();
}
