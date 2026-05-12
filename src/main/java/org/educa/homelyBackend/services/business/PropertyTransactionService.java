package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyTransactionModel;

public interface PropertyTransactionService {
    PropertyTransactionModel findByName(String name);
}
