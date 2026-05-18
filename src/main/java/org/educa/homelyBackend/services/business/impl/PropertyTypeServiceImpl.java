package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyTypeDao;
import org.educa.homelyBackend.models.PropertyTypeModel;
import org.educa.homelyBackend.services.business.PropertyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyTypeServiceImpl implements PropertyTypeService {

    private final PropertyTypeDao propertyTypeDao;

    @Override
    public List<PropertyTypeModel> findAll() {
        return propertyTypeDao.findAll();
    }
}
