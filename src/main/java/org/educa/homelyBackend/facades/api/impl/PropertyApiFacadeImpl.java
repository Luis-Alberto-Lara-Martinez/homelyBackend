package org.educa.homelyBackend.facades.api.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.PropertyAddressResponse;
import org.educa.homelyBackend.dtos.responses.PropertyExtraResponse;
import org.educa.homelyBackend.dtos.responses.PropertyImageResponse;
import org.educa.homelyBackend.dtos.responses.PropertyResponse;
import org.educa.homelyBackend.dtos.responses.ResidenceResponse;
import org.educa.homelyBackend.facades.api.PropertyApiFacade;
import org.educa.homelyBackend.models.PropertyAddressModel;
import org.educa.homelyBackend.models.PropertyImageModel;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.models.ResidenceModel;
import org.educa.homelyBackend.services.business.PropertyService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyApiFacadeImpl implements PropertyApiFacade {
    @Override
    public List<PropertyResponse> findAllPropertiesWithDetails() {
        return List.of();
    }
}


