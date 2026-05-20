package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.EnergyCertificateDto;
import org.educa.homelyBackend.dtos.PropertyAddressDto;
import org.educa.homelyBackend.dtos.PropertyDto;
import org.educa.homelyBackend.dtos.PropertyExtraDto;
import org.educa.homelyBackend.dtos.PropertyImageDto;
import org.educa.homelyBackend.dtos.ResidenceDto;
import org.educa.homelyBackend.dtos.requests.CreatePropertyDtoRequest;
import org.educa.homelyBackend.dtos.requests.GeneratePropertyDescriptionByAIDtoRequest;
import org.educa.homelyBackend.dtos.requests.PageDtoRequest;
import org.educa.homelyBackend.facades.business.PropertyFacade;
import org.educa.homelyBackend.models.EnergyCertificateModel;
import org.educa.homelyBackend.models.PropertyAddressModel;
import org.educa.homelyBackend.models.PropertyExtraModel;
import org.educa.homelyBackend.models.PropertyImageModel;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.models.ResidenceModel;
import org.educa.homelyBackend.services.business.PropertyAddressService;
import org.educa.homelyBackend.services.business.PropertyExtraService;
import org.educa.homelyBackend.services.business.PropertyImageService;
import org.educa.homelyBackend.services.business.PropertyService;
import org.educa.homelyBackend.services.business.PropertyStatusService;
import org.educa.homelyBackend.services.business.PropertyTransactionService;
import org.educa.homelyBackend.services.business.PropertyTypeService;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.business.EnergyCertificateService;
import org.educa.homelyBackend.services.business.ResidenceService;
import org.educa.homelyBackend.services.shared.CloudinaryService;
import org.educa.homelyBackend.services.shared.GroqService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PropertyFacadeImpl implements PropertyFacade {

    private final PropertyService propertyService;
    private final PropertyAddressService propertyAddressService;
    private final PropertyTransactionService propertyTransactionService;
    private final PropertyTypeService propertyTypeService;
    private final PropertyStatusService propertyStatusService;
    private final UserService userService;
    private final PropertyImageService propertyImageService;
    private final CloudinaryService cloudinaryService;
    private final PropertyExtraService propertyExtraService;
    private final EnergyCertificateService energyCertificateService;
    private final ResidenceService residenceService;
    private final GroqService groqService;

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

                    EnergyCertificateDto energyCertificateDto = null;
                    if (property.getEnergyCertificate() != null) {
                        energyCertificateDto = EnergyCertificateDto.builder()
                                .hasCertificate(property.getEnergyCertificate().getHasCertificate())
                                .consumptionScale(property.getEnergyCertificate().getConsumptionScale())
                                .consumptionValue(property.getEnergyCertificate().getConsumptionValue())
                                .emissionsScale(property.getEnergyCertificate().getEmissionsScale())
                                .emissionsValue(property.getEnergyCertificate().getEmissionsValue())
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
                            .energyCertificate(energyCertificateDto)
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

                    EnergyCertificateDto energyCertificateDto = null;
                    if (address.getProperty().getEnergyCertificate() != null) {
                        energyCertificateDto = EnergyCertificateDto.builder()
                                .hasCertificate(address.getProperty().getEnergyCertificate().getHasCertificate())
                                .consumptionScale(address.getProperty().getEnergyCertificate().getConsumptionScale())
                                .consumptionValue(address.getProperty().getEnergyCertificate().getConsumptionValue())
                                .emissionsScale(address.getProperty().getEnergyCertificate().getEmissionsScale())
                                .emissionsValue(address.getProperty().getEnergyCertificate().getEmissionsValue())
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
                            .energyCertificate(energyCertificateDto)
                            .build();
                }).toList();
    }

    @Override
    public PropertyDto findPropertyById(Integer id) {
        PropertyModel property = propertyService.findByIdOrThrow(id);


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
                .updatedBy(
                        property.getUpdatedBy() != null
                                ? property.getUpdatedBy().getName()
                                : null
                )
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
                .residence(
                        property.getResidence() != null
                                ? ResidenceDto.builder()
                                .bedrooms(property.getResidence().getBedrooms())
                                .bathrooms(property.getResidence().getBathrooms())
                                .conservation(property.getResidence().getConservation())
                                .orientation(property.getResidence().getOrientation())
                                .build()
                                : null
                )
                .energyCertificate(
                        property.getEnergyCertificate() != null
                                ? EnergyCertificateDto.builder()
                                .hasCertificate(property.getEnergyCertificate().getHasCertificate())
                                .consumptionScale(property.getEnergyCertificate().getConsumptionScale())
                                .consumptionValue(property.getEnergyCertificate().getConsumptionValue())
                                .emissionsScale(property.getEnergyCertificate().getEmissionsScale())
                                .emissionsValue(property.getEnergyCertificate().getEmissionsValue())
                                .build()
                                : null
                )
                .build();
    }

    @Override
    public void saveProperty(String creatorEmail, CreatePropertyDtoRequest request) {
        // 1. Construimos la entidad principal (PropertyModel) sin las relaciones OneToOne todavía
        PropertyModel propertyModel = PropertyModel.builder()
                .type(propertyTypeService.findByNameOrThrow(request.type()))
                .status(propertyStatusService.findByNameOrThrow(request.status()))
                .transaction(propertyTransactionService.findByNameOrThrow(request.transaction()))
                .title(request.title())
                .description(request.description())
                .surface(request.surface())
                .price(request.price())
                .updatedBy(userService.findByEmailOrThrow(creatorEmail))
                        // extras will be linked after saving the property (owner side is PropertyExtraModel)
                .build();

        // 2. Construimos el Certificado Energético (no lo vinculamos todavía)
        EnergyCertificateModel energyCertificate = null;
        if (request.energyCertificate() != null) {
            energyCertificate = EnergyCertificateModel.builder()
                    .hasCertificate(request.energyCertificate().hasCertificate())
                    .consumptionScale(request.energyCertificate().consumptionScale())
                    .consumptionValue(request.energyCertificate().consumptionValue())
                    .emissionsScale(request.energyCertificate().emissionsScale())
                    .emissionsValue(request.energyCertificate().emissionsValue())
                    .build();
        }

        // 3. Construimos y vinculamos la Dirección (PropertyAddressModel)
        PropertyAddressModel address = null;
        if (request.address() != null) {
            address = PropertyAddressModel.builder()
                    .street(request.address().street())
                    .number(request.address().number())
                    .floor(request.address().floor())
                    .door(request.address().door())
                    .postalCode(request.address().postalCode())
                    .city(request.address().city())
                    .province(request.address().province())
                    .country(request.address().country())
                    .latitude(request.address().latitude())
                    .longitude(request.address().longitude())
                    .build();
        }

        // 4. Construimos y vinculamos los datos de Residencia (si aplican al tipo de propiedad)
        ResidenceModel residence = null;
        if (request.residence() != null) {
            residence = ResidenceModel.builder()
                    .bedrooms(request.residence().bedrooms())
                    .bathrooms(request.residence().bathrooms())
                    .conservation(request.residence().conservation())
                    .orientation(request.residence().orientation())
                    .build();
        }

        // 5. Guardamos la propiedad para obtener el id y que las relaciones se puedan persistir
        PropertyModel savedProperty = propertyService.save(propertyModel);

        // 6. Asociamos los extras (lado propietario) y los guardamos
        if (request.extras() != null && !request.extras().isEmpty()) {
            for (var extraDto : request.extras()) {
                PropertyExtraModel extra = propertyExtraService.findByNameOrThrow(extraDto.name());
                extra.getProperties().add(savedProperty);
                propertyExtraService.save(extra);
            }
        }

        // 7. Subimos y persistimos las imágenes (si las hay)
        if (request.images() != null && !request.images().isEmpty()) {
            var images = request.images().stream()
                    .map(imageRequest -> {
                        String imageUrl = cloudinaryService.uploadPropertyImage(imageRequest.image(), savedProperty.getId(), imageRequest.displayOrder());
                        PropertyImageModel imageModel = PropertyImageModel.builder()
                                .property(savedProperty)
                                .imageUrl(imageUrl)
                                .displayOrder(imageRequest.displayOrder())
                                .build();
                        return propertyImageService.save(imageModel);
                    }).collect(Collectors.toSet());

            savedProperty.setPropertyImages(images);
        }

        // 9. Guardamos las entidades dependientes que usan @MapsId (address, residence, energyCertificate)
        if (address != null) {
            address.setProperty(savedProperty);
            PropertyAddressModel savedAddress = propertyAddressService.save(address);
            savedProperty.setPropertyAddress(savedAddress);
        }

        if (residence != null) {
            residence.setProperty(savedProperty);
            ResidenceModel savedResidence = residenceService.save(residence);
            savedProperty.setResidence(savedResidence);
        }

        if (energyCertificate != null) {
            energyCertificate.setProperty(savedProperty);
            EnergyCertificateModel savedEnergy = energyCertificateService.save(energyCertificate);
            savedProperty.setEnergyCertificate(savedEnergy);
        }

        propertyService.save(savedProperty);
    }

    @Override
    public void deletePropertyById(Integer id) {
        propertyService.deleteById(id);
    }

    @Override
    public List<PropertyExtraDto> findAllPropertyExtraById(Integer id) {
        return propertyExtraService.findByPropertyType(propertyTypeService.findById(id))
                .stream()
                .map(extra -> PropertyExtraDto.builder()
                        .id(extra.getId())
                        .name(extra.getName())
                        .build())
                .toList();
    }

    @Override
    public String generateDescription(GeneratePropertyDescriptionByAIDtoRequest request) {
        return groqService.generatePropertyDescription(request.toString());
    }
}
