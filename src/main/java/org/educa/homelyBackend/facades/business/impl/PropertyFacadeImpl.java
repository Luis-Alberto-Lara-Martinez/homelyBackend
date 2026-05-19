package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.PropertyAddressDto;
import org.educa.homelyBackend.dtos.PropertyDto;
import org.educa.homelyBackend.dtos.PropertyExtraDto;
import org.educa.homelyBackend.dtos.ResidenceDto;
import org.educa.homelyBackend.dtos.requests.PageDtoRequest;
import org.educa.homelyBackend.dtos.PropertyImageDto;
import org.educa.homelyBackend.facades.business.PropertyFacade;
import org.educa.homelyBackend.services.business.PropertyAddressService;
import org.educa.homelyBackend.services.business.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyFacadeImpl implements PropertyFacade {

    private final PropertyService propertyService;
    private final PropertyAddressService propertyAddressService;

    @Override
    public Page<PropertyDto> findAllProperties(PageDtoRequest request) {
        return propertyService.findAll(request.page(), request.size())
                .map(property -> {
                    ResidenceDto residence = null;
                    if (property.getResidence() != null) {
                        residence = ResidenceDto.builder()
                                .bedrooms(property.getResidence().getBedrooms())
                                .bathrooms(property.getResidence().getBathrooms())
                                .conservation(property.getResidence().getConservation())
                                .orientation(property.getResidence().getOrientation())
                                .build();
                    }

                    return PropertyDto.builder()
                            .id(property.getId())
                            .type(property.getType().getName())
                            .status(property.getStatus().getName())
                            .transaction(property.getTransaction().getName())
                            .title(property.getTitle())
                            .description(property.getDescription())
                            .surface(property.getSurface())
                            .price(property.getPrice())
                            .createdAt(property.getCreatedAt())
                            .updatedAt(property.getUpdatedAt())
                            .updatedBy(property.getUpdatedBy().getName())
                            .images(property.getPropertyImages().stream()
                                    .map(image -> PropertyImageDto.builder()
                                            .id(image.getId())
                                            .imageUrl(image.getImageUrl())
                                            .displayOrder(image.getDisplayOrder())
                                            .build())
                                    .toList())
                            .extras(property.getPropertyExtras().stream()
                                    .map(extra -> PropertyExtraDto.builder()
                                            .id(extra.getId())
                                            .name(extra.getName())
                                            .build())
                                    .toList())
                            .address(PropertyAddressDto.builder()
                                    .street(property.getPropertyAddress().getStreet())
                                    .number(property.getPropertyAddress().getNumber())
                                    .floor(property.getPropertyAddress().getFloor())
                                    .door(property.getPropertyAddress().getDoor())
                                    .postalCode(property.getPropertyAddress().getPostalCode())
                                    .city(property.getPropertyAddress().getCity())
                                    .province(property.getPropertyAddress().getProvince())
                                    .country(property.getPropertyAddress().getCountry())
                                    .latitude(property.getPropertyAddress().getLatitude())
                                    .longitude(property.getPropertyAddress().getLongitude())
                                    .build())
                            .residence(residence)
                            .build();
                });
    }

    @Override
    public List<PropertyDto> findPropertiesWithinRadius(BigDecimal latitude, BigDecimal longitude, Integer radiusKm) {
        return propertyAddressService.findByLatitudeAndLongitudeWithinRadius(latitude, longitude, radiusKm)
                .stream()
                .map(address -> {
                    ResidenceDto residence = null;
                    if (address.getProperty().getResidence() != null) {
                        residence = ResidenceDto.builder()
                                .bedrooms(address.getProperty().getResidence().getBedrooms())
                                .bathrooms(address.getProperty().getResidence().getBathrooms())
                                .conservation(address.getProperty().getResidence().getConservation())
                                .orientation(address.getProperty().getResidence().getOrientation())
                                .build();
                    }
                    return PropertyDto.builder()
                            .id(address.getProperty().getId())
                            .type(address.getProperty().getType().getName())
                            .status(address.getProperty().getStatus().getName())
                            .transaction(address.getProperty().getTransaction().getName())
                            .title(address.getProperty().getTitle())
                            .description(address.getProperty().getDescription())
                            .surface(address.getProperty().getSurface())
                            .price(address.getProperty().getPrice())
                            .createdAt(address.getProperty().getCreatedAt())
                            .updatedAt(address.getProperty().getUpdatedAt())
                            .updatedBy(address.getProperty().getUpdatedBy().getName())
                            .images(address.getProperty().getPropertyImages().stream()
                                    .map(image -> PropertyImageDto.builder()
                                            .id(image.getId())
                                            .imageUrl(image.getImageUrl())
                                            .displayOrder(image.getDisplayOrder())
                                            .build())
                                    .toList())
                            .extras(address.getProperty().getPropertyExtras().stream()
                                    .map(extra -> PropertyExtraDto.builder()
                                            .id(extra.getId())
                                            .name(extra.getName())
                                            .build())
                                    .toList())
                            .address(PropertyAddressDto.builder()
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
                }).toList();
    }
}
