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

    private static final String RESIDENCE_TYPE = "residencia";

    private final PropertyService propertyService;

    @Override
    public List<PropertyResponse> findAllPropertiesWithDetails() {
        return propertyService.findAllWithDetails().stream().map(this::toResponse).toList();
    }

    private PropertyResponse toResponse(PropertyModel property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .user(property.getUser().getName())
                .type(property.getType().getName())
                .status(property.getStatus().getName())
                .transaction(property.getTransaction().getName())
                .title(property.getTitle())
                .description(property.getDescription())
                .surface(property.getSurface())
                .price(property.getPrice())
                .images(property.getPropertyImages().stream()
                        .sorted(Comparator.comparing(PropertyImageModel::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                        .map(image -> PropertyImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .displayOrder(image.getDisplayOrder())
                                .build())
                        .toList())
                .extras(property.getPropertyExtras().stream()
                        .map(extra -> PropertyExtraResponse.builder()
                                .id(extra.getId())
                                .name(extra.getName())
                                .build())
                        .sorted(Comparator.comparing(PropertyExtraResponse::name, Comparator.nullsLast(String::compareToIgnoreCase)))
                        .toList())
                .address(toAddressResponse(property.getPropertyAddress()))
                .residence(toResidenceResponse(property))
                .build();
    }

    private PropertyAddressResponse toAddressResponse(PropertyAddressModel address) {
        if (address == null) {
            return null;
        }

        return PropertyAddressResponse.builder()
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
                .build();
    }

    private ResidenceResponse toResidenceResponse(PropertyModel property) {
        if (property.getType() == null || property.getType().getName() == null
                || !RESIDENCE_TYPE.equalsIgnoreCase(property.getType().getName())) {
            return null;
        }

        ResidenceModel residence = property.getResidence();

        if (residence == null) {
            return null;
        }

        return ResidenceResponse.builder()
                .bedrooms(residence.getBedrooms())
                .bathrooms(residence.getBathrooms())
                .conservation(residence.getConservation())
                .orientation(residence.getOrientation())
                .build();
    }
}


