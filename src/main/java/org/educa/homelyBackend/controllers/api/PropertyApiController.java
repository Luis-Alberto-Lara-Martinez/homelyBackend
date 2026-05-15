package org.educa.homelyBackend.controllers.api;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.PropertyResponse;
import org.educa.homelyBackend.facades.api.PropertyApiFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.API + "/properties")
@RequiredArgsConstructor
public class PropertyApiController {

    private final PropertyApiFacade propertyApiFacade;

    @GetMapping("/")
    public List<PropertyResponse> findAllPropertiesWithDetails() {
        return propertyApiFacade.findAllPropertiesWithDetails();
    }
}

