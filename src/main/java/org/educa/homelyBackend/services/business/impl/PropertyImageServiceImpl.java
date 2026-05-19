package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyImageDao;
import org.educa.homelyBackend.models.PropertyImageModel;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.services.business.PropertyImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyImageServiceImpl implements PropertyImageService {

    private final PropertyImageDao propertyImageDao;

    @Override
    public List<PropertyImageModel> findAllByPropertyId(PropertyModel property) {
        return propertyImageDao.findAllByProperty(property);
    }

    @Override
    public PropertyImageModel save(PropertyImageModel propertyImageModel) {
        return propertyImageDao.save(propertyImageModel);
    }

    @Override
    public void delete(PropertyImageModel propertyImageModel) {
        propertyImageDao.delete(propertyImageModel);
    }
}
