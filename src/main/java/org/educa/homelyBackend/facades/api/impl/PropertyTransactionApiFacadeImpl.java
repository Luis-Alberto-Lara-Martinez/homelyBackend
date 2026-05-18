package org.educa.homelyBackend.facades.api.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.PropertyTransactionResponse;
import org.educa.homelyBackend.facades.api.PropertyTransactionApiFacade;
import org.educa.homelyBackend.services.business.PropertyTransactionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyTransactionApiFacadeImpl implements PropertyTransactionApiFacade {

    private final PropertyTransactionService propertyTransactionService;

    @Override
    public List<PropertyTransactionResponse> findAll() {
        return propertyTransactionService.findAll().stream().map(propertyTransactionModel -> PropertyTransactionResponse.builder()
                .id(propertyTransactionModel.getId())
                .name(propertyTransactionModel.getName())
                .build()).toList();
    }
}


