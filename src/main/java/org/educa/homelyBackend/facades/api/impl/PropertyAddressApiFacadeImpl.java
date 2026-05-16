package org.educa.homelyBackend.facades.api.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.PropertyAddressResponse;
import org.educa.homelyBackend.dtos.responses.PropertyExtraResponse;
import org.educa.homelyBackend.dtos.responses.PropertyImageResponse;
import org.educa.homelyBackend.dtos.responses.PropertyResponse;
import org.educa.homelyBackend.dtos.responses.ResidenceResponse;
import org.educa.homelyBackend.facades.api.PropertyAddressApiFacade;
import org.educa.homelyBackend.models.PropertyAddressModel;
import org.educa.homelyBackend.services.business.PropertyAddressService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyAddressApiFacadeImpl implements PropertyAddressApiFacade {

    private final PropertyAddressService propertyAddressService;

    @Override
    public List<PropertyResponse> findAddressesWithinRadius(double latitude, double longitude, Integer radiusKm) {
        List<PropertyAddressModel> addresses = propertyAddressService.findAddressesWithinRadius(latitude, longitude, radiusKm);
        List<PropertyResponse> properties = new ArrayList<>();

        for (PropertyAddressModel address : addresses) {
            ResidenceResponse residence = null;

            if (address.getProperty().getResidence() != null) {
                residence = ResidenceResponse.builder()
                        .bedrooms(address.getProperty().getResidence().getBedrooms())
                        .bathrooms(address.getProperty().getResidence().getBathrooms())
                        .conservation(address.getProperty().getResidence().getConservation())
                        .orientation(address.getProperty().getResidence().getOrientation())
                        .build();
            }

            PropertyResponse property = PropertyResponse.builder()
                    .id(address.getProperty().getId())
                    .user(address.getProperty().getUser().getName())
                    .type(address.getProperty().getType().getName())
                    .status(address.getProperty().getStatus().getName())
                    .transaction(address.getProperty().getTransaction().getName())
                    .title(address.getProperty().getTitle())
                    .description(address.getProperty().getDescription())
                    .surface(address.getProperty().getSurface())
                    .price(address.getProperty().getPrice())
                    .images(address.getProperty().getPropertyImages().stream().map(image -> PropertyImageResponse.builder()
                            .id(image.getId())
                            .imageUrl(image.getImageUrl())
                            .displayOrder(image.getDisplayOrder())
                            .build()).toList())
                    .extras(address.getProperty().getPropertyExtras().stream().map(extra -> PropertyExtraResponse.builder()
                            .id(extra.getId())
                            .name(extra.getName())
                            .build()).toList())
                    .address(PropertyAddressResponse.builder()
                            .street(address.getStreet())
                            .number(address.getNumber())
                            .floor(address.getFloor())
                            .door(address.getDoor())
                            .postalCode(address.getPostalCode())
                            .city(address.getCity())
                            .province(address.getProvince())
                            .country(address.getCountry())
                            .latitude(address.getLatitude())
                            .longitude(address.getLongitude())
                            .build())
                    .residence(residence)
                    .build();
            properties.add(property);
        }

        return properties;
    }
}


