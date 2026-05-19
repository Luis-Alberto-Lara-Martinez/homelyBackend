package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyStatusDao;
import org.educa.homelyBackend.models.PropertyStatusModel;
import org.educa.homelyBackend.services.business.PropertyStatusService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyStatusServiceImpl implements PropertyStatusService {

    private final PropertyStatusDao propertyStatusDao;

    @Override
    public PropertyStatusModel findByNameOrThrow(String name) {
        return propertyStatusDao.findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el estado de propiedad con el nombre: " + name
                ).get());
    }

    @Override
    public List<PropertyStatusModel> findAll() {
        return propertyStatusDao.findAll();
    }
}
