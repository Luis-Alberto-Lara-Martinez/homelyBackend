package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyDao;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.services.business.PropertyService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyDao propertyDao;

    @Override
    public PropertyModel save(PropertyModel propertyModel) {
        return propertyDao.save(propertyModel);
    }
}
