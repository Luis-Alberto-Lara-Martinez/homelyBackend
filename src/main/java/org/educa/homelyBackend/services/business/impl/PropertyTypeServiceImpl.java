package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyTypeDao;
import org.educa.homelyBackend.models.PropertyTypeModel;
import org.educa.homelyBackend.services.business.PropertyTypeService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyTypeServiceImpl implements PropertyTypeService {

    private final PropertyTypeDao propertyTypeDao;

    @Override
    public PropertyTypeModel findByNameOrThrow(String name) {
        return propertyTypeDao.findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "El tipo de propiedad con el nombre '" + name + "' no fue encontrado."
                ).get());
    }

    @Override
    public List<PropertyTypeModel> findAll() {
        return propertyTypeDao.findAll();
    }
}
