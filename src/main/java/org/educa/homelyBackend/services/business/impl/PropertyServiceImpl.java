package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyDao;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.services.business.PropertyService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyDao propertyDao;

    @Override
    public PropertyModel findByIdOrThrow(Integer id) {
        return propertyDao.findById(id)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "Propiedad no encontrada con ID: " + id
                ).get());
    }

    @Override
    public Page<PropertyModel> findAll(Integer page, Integer size) {
        return propertyDao.findAll(PageRequest.of(page, size));
    }

    @Override
    public PropertyModel save(PropertyModel propertyModel) {
        return propertyDao.save(propertyModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        propertyDao.deleteById(id);
    }
}
