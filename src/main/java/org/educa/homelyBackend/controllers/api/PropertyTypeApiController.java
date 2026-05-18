package org.educa.homelyBackend.controllers.api;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.PropertyTypeResponse;
import org.educa.homelyBackend.facades.api.PropertyTypeApiFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.API + "/properties/types")
@RequiredArgsConstructor
public class PropertyTypeApiController {

    private final PropertyTypeApiFacade propertyTypeApiFacade;

    @GetMapping("")
    public List<PropertyTypeResponse> findAll() {
        return propertyTypeApiFacade.findAll();
    }
}

