package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.models.PropertyStatusModel;
import org.educa.homelyBackend.models.PropertyTransactionModel;
import org.educa.homelyBackend.models.PropertyTypeModel;
import org.educa.homelyBackend.models.UserModel;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyService {
    Page<PropertyModel> findAll(Integer page, Integer size, String sortBy);

    List<PropertyModel> findAllWithDetails();

    PropertyModel save(PropertyModel propertyModel);

    PropertyModel create(
            UserModel user, PropertyTypeModel propertyType, PropertyStatusModel propertyStatus,
            PropertyTransactionModel propertyTransaction, String title, String description,
            BigDecimal price
    );
}
