package org.educa.homelyBackend.facades.api.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.PropertyTypeResponse;
import org.educa.homelyBackend.facades.api.PropertyTypeApiFacade;
import org.educa.homelyBackend.services.business.PropertyTypeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyTypeApiFacadeImpl implements PropertyTypeApiFacade {

    private final PropertyTypeService propertyTypeService;

    @Override
    public List<PropertyTypeResponse> findAll() {
        return propertyTypeService.findAll().stream().map(propertyTypeModel -> PropertyTypeResponse.builder()
                .id(propertyTypeModel.getId())
                .name(propertyTypeModel.getName())
                .build()).toList();
    }
}


