package org.educa.homelyBackend.facades.api;

import org.educa.homelyBackend.dtos.responses.PropertyTransactionResponse;

import java.util.List;

public interface PropertyTransactionApiFacade {
    List<PropertyTransactionResponse> findAll();
}
