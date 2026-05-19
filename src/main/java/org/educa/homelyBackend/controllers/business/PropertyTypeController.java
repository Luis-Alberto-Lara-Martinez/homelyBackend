package org.educa.homelyBackend.controllers.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.PropertyTypeDto;
import org.educa.homelyBackend.facades.business.PropertyTypeFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.API + "/properties/types")
@RequiredArgsConstructor
public class PropertyTypeController {

    private final PropertyTypeFacade propertyTypeFacade;

    @GetMapping("")
    public List<PropertyTypeDto> findAll() {
        return propertyTypeFacade.findAll();
    }
}

