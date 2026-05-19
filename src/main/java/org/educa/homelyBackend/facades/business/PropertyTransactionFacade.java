package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.PropertyTransactionDto;

import java.util.List;

public interface PropertyTransactionFacade {
    List<PropertyTransactionDto> findAll();
}
