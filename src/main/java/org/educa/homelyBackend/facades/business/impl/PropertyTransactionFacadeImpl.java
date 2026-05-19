package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.PropertyTransactionDto;
import org.educa.homelyBackend.facades.business.PropertyTransactionFacade;
import org.educa.homelyBackend.services.business.PropertyTransactionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyTransactionFacadeImpl implements PropertyTransactionFacade {

    private final PropertyTransactionService propertyTransactionService;

    @Override
    public List<PropertyTransactionDto> findAll() {
        return propertyTransactionService.findAll()
                .stream()
                .map(propertyTransactionModel -> PropertyTransactionDto.builder()
                        .id(propertyTransactionModel.getId())
                        .name(propertyTransactionModel.getName())
                        .build()
                ).toList();
    }
}


