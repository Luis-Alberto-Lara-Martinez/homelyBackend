package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyTransactionDao;
import org.educa.homelyBackend.models.PropertyTransactionModel;
import org.educa.homelyBackend.services.business.PropertyTransactionService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyTransactionServiceImpl implements PropertyTransactionService {

    private final PropertyTransactionDao propertyTransactionDao;

    @Override
    public PropertyTransactionModel findByNameOrThrow(String name) {
        return propertyTransactionDao.findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "La transacción de propiedad con el nombre '" + name + "' no fue encontrada."
                ).get());
    }

    @Override
    public List<PropertyTransactionModel> findAll() {
        return propertyTransactionDao.findAll();
    }
}
