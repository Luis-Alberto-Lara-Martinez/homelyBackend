package org.educa.homelyBackend.controllers.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.PropertyTransactionDto;
import org.educa.homelyBackend.facades.business.PropertyTransactionFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.API + "/properties/transactions")
@RequiredArgsConstructor
public class PropertyTransactionController {

    private final PropertyTransactionFacade propertyTransactionFacade;

    @GetMapping("")
    public List<PropertyTransactionDto> findAll() {
        return propertyTransactionFacade.findAll();
    }
}

