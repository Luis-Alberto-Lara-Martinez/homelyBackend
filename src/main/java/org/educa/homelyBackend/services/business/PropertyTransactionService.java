package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyTransactionModel;

import java.util.List;

public interface PropertyTransactionService {
    PropertyTransactionModel findByNameOrThrow(String name);

    List<PropertyTransactionModel> findAll();
}
