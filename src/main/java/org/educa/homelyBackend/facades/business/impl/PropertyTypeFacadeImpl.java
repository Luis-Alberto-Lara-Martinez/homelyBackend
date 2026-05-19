package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.PropertyTypeDto;
import org.educa.homelyBackend.facades.business.PropertyTypeFacade;
import org.educa.homelyBackend.services.business.PropertyTypeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyTypeFacadeImpl implements PropertyTypeFacade {

    private final PropertyTypeService propertyTypeService;

    @Override
    public List<PropertyTypeDto> findAll() {
        return propertyTypeService.findAll()
                .stream()
                .map(propertyTypeModel -> PropertyTypeDto.builder()
                        .id(propertyTypeModel.getId())
                        .name(propertyTypeModel.getName())
                        .build()
                ).toList();
    }
}
