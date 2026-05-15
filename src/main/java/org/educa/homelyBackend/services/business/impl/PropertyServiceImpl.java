package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyDao;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.models.PropertyStatusModel;
import org.educa.homelyBackend.models.PropertyTransactionModel;
import org.educa.homelyBackend.models.PropertyTypeModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.PropertyService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyDao propertyDao;

    @Override
    public Page<PropertyModel> findAll(Integer page, Integer size, String sortBy) {
        if (page == null || page - 1 < 0) {
            page = 1;
        }

        if (size == null || size <= 0) {
            size = 30;
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }

        Page<PropertyModel> pagedProperties = propertyDao.findAll(PageRequest.of(page - 1, size, Sort.by(sortBy)));


        if (pagedProperties.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No existe ninguna propiedad").get();
        }

        return pagedProperties;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyModel> findAllWithDetails() {
        List<PropertyModel> properties = propertyDao.findAllWithDetails();

        if (properties.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No existe ninguna propiedad").get();
        }

        return properties;
    }

    @Override
    public PropertyModel save(PropertyModel propertyModel) {
        return propertyDao.save(propertyModel);
    }

    @Override
    public PropertyModel create(UserModel user, PropertyTypeModel propertyType, PropertyStatusModel propertyStatus, PropertyTransactionModel propertyTransaction, String title, String description, BigDecimal price) {
        return propertyDao.save(
                PropertyModel.builder()
                        .user(user)
                        .type(propertyType)
                        .status(propertyStatus)
                        .transaction(propertyTransaction)
                        .title(title)
                        .description(description)
                        .price(price)
                        .build()
        );
    }
}
