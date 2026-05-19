package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyExtraDao;
import org.educa.homelyBackend.models.PropertyExtraModel;
import org.educa.homelyBackend.models.PropertyTypeModel;
import org.educa.homelyBackend.services.business.PropertyExtraService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PropertyExtraServiceImpl implements PropertyExtraService {

    private final PropertyExtraDao propertyExtraDao;

    @Override
    public PropertyExtraModel findByNameOrThrow(String name) {
        return propertyExtraDao.findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el extra de propiedad con el nombre: " + name
                ).get());
    }

    @Override
    public List<PropertyExtraModel> findByPropertyType(PropertyTypeModel propertyTypeModel) {
        return propertyExtraDao.findByPropertyTypes(Set.of(propertyTypeModel));
    }
}
