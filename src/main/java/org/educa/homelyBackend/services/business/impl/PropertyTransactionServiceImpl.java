package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyTransactionDao;
import org.educa.homelyBackend.models.PropertyTransactionModel;
import org.educa.homelyBackend.services.business.PropertyTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyTransactionServiceImpl implements PropertyTransactionService {

    private final PropertyTransactionDao propertyTransactionDao;

    @Override
    public List<PropertyTransactionModel> findAll() {
        return propertyTransactionDao.findAll();
    }
}
