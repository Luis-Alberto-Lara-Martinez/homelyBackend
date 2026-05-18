package org.educa.homelyBackend.controllers.api;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.PropertyTransactionResponse;
import org.educa.homelyBackend.facades.api.PropertyTransactionApiFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.API + "/properties/transactions")
@RequiredArgsConstructor
public class PropertyTransactionApiController {

    private final PropertyTransactionApiFacade propertyTransactionApiFacade;

    @GetMapping("")
    public List<PropertyTransactionResponse> findAll() {
        return propertyTransactionApiFacade.findAll();
    }
}

