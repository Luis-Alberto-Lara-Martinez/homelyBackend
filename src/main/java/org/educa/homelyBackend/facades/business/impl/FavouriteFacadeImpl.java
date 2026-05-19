package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.EnergyCertificateDto;
import org.educa.homelyBackend.dtos.FavouriteDto;
import org.educa.homelyBackend.dtos.PropertyAddressDto;
import org.educa.homelyBackend.dtos.PropertyDto;
import org.educa.homelyBackend.dtos.PropertyExtraDto;
import org.educa.homelyBackend.dtos.PropertyImageDto;
import org.educa.homelyBackend.dtos.ResidenceDto;
import org.educa.homelyBackend.facades.business.FavouriteFacade;
import org.educa.homelyBackend.models.FavouriteModel;
import org.educa.homelyBackend.services.business.FavouriteService;
import org.educa.homelyBackend.services.business.PropertyService;
import org.educa.homelyBackend.services.business.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FavouriteFacadeImpl implements FavouriteFacade {

    private final FavouriteService favouriteService;
    private final UserService userService;
    private final PropertyService propertyService;

    @Override
    public void save(String email, Integer propertyId) {
        favouriteService.save(FavouriteModel.builder()
                .user(userService.findByEmailOrThrow(email))
                .property(propertyService.findByIdOrThrow(propertyId))
                .build()
        );
    }

    @Override
    public void delete(String email, Integer propertyId) {
        favouriteService.deleteByUserAndProperty(userService.findByEmailOrThrow(email), propertyService.findByIdOrThrow(propertyId));
    }

    @Override
    public List<FavouriteDto> findAllByUser(String email) {
        return favouriteService.findAllByUser(userService.findByEmailOrThrow(email))
                .stream()
                .map(favourite -> {
                    ResidenceDto residence = null;
                    if (favourite.getProperty().getResidence() != null) {
                        residence = ResidenceDto.builder()
                                .bedrooms(favourite.getProperty().getResidence().getBedrooms())
                                .bathrooms(favourite.getProperty().getResidence().getBathrooms())
                                .conservation(favourite.getProperty().getResidence().getConservation())
                                .orientation(favourite.getProperty().getResidence().getOrientation())
                                .build();
                    }

                    EnergyCertificateDto energyCertificateDto = null;
                    if (favourite.getProperty().getEnergyCertificate() != null) {
                        energyCertificateDto = EnergyCertificateDto.builder()
                                .hasCertificate(favourite.getProperty().getEnergyCertificate().getHasCertificate())
                                .consumptionScale(favourite.getProperty().getEnergyCertificate().getConsumptionScale())
                                .consumptionValue(favourite.getProperty().getEnergyCertificate().getConsumptionValue())
                                .emissionsScale(favourite.getProperty().getEnergyCertificate().getEmissionsScale())
                                .emissionsValue(favourite.getProperty().getEnergyCertificate().getEmissionsValue())
                                .build();
                    }

                    return FavouriteDto.builder()
                            .id(favourite.getId())
                            .property(PropertyDto.builder()
                                    .id(favourite.getProperty().getId())
                                    .type(favourite.getProperty().getType().getName())
                                    .status(favourite.getProperty().getStatus().getName())
                                    .transaction(favourite.getProperty().getTransaction().getName())
                                    .title(favourite.getProperty().getTitle())
                                    .description(favourite.getProperty().getDescription())
                                    .surface(favourite.getProperty().getSurface())
                                    .price(favourite.getProperty().getPrice())
                                    .createdAt(favourite.getProperty().getCreatedAt())
                                    .updatedAt(favourite.getProperty().getUpdatedAt())
                                    .images(favourite.getProperty().getPropertyImages().stream()
                                            .map(image -> PropertyImageDto.builder()
                                                    .id(image.getId())
                                                    .imageUrl(image.getImageUrl())
                                                    .displayOrder(image.getDisplayOrder())
                                                    .build())
                                            .toList())
                                    .extras(favourite.getProperty().getPropertyExtras().stream()
                                            .map(extra -> PropertyExtraDto.builder()
                                                    .id(extra.getId())
                                                    .name(extra.getName())
                                                    .build())
                                            .toList())
                                    .address(PropertyAddressDto.builder()
                                            .street(favourite.getProperty().getPropertyAddress().getStreet())
                                            .number(favourite.getProperty().getPropertyAddress().getNumber())
                                            .floor(favourite.getProperty().getPropertyAddress().getFloor())
                                            .door(favourite.getProperty().getPropertyAddress().getDoor())
                                            .postalCode(favourite.getProperty().getPropertyAddress().getPostalCode())
                                            .city(favourite.getProperty().getPropertyAddress().getCity())
                                            .province(favourite.getProperty().getPropertyAddress().getProvince())
                                            .country(favourite.getProperty().getPropertyAddress().getCountry())
                                            .latitude(favourite.getProperty().getPropertyAddress().getLatitude())
                                            .longitude(favourite.getProperty().getPropertyAddress().getLongitude())
                                            .build())
                                    .residence(residence)
                                    .energyCertificate(energyCertificateDto)
                                    .build()
                            ).build();
                }).toList();
    }
}
