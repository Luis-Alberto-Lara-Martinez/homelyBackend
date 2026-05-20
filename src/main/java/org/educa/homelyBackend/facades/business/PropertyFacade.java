package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.PropertyDto;
import org.educa.homelyBackend.dtos.requests.CreatePropertyDtoRequest;
import org.educa.homelyBackend.dtos.requests.PageDtoRequest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyFacade {
    Page<PropertyDto> findAllProperties(PageDtoRequest request);

    List<PropertyDto> findPropertiesWithinRadius(
            BigDecimal latitude,
            BigDecimal longitude,
            Integer radiusKm
    );

    PropertyDto findPropertyById(Integer id);

    void saveProperty(String creatorEmail, CreatePropertyDtoRequest request);

    void deletePropertyById(Integer id);
}
