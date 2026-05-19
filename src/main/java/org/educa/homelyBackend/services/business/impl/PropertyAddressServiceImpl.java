package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyAddressDao;
import org.educa.homelyBackend.models.PropertyAddressModel;
import org.educa.homelyBackend.services.business.PropertyAddressService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PropertyAddressServiceImpl implements PropertyAddressService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final PropertyAddressDao propertyAddressDao;

    @Override
    public List<PropertyAddressModel> findByLatitudeAndLongitudeWithinRadius(
            BigDecimal latitude, BigDecimal longitude, Integer radiusKm
    ) {
        double latCenter = latitude.doubleValue();
        double lonCenter = longitude.doubleValue();
        double searchRadiusKm = radiusKm.doubleValue();

        Map<String, Double> boundingBox = calculateBoundingBox(latCenter, lonCenter, searchRadiusKm);

        return propertyAddressDao.findAllByLatitudeBetweenAndLongitudeBetween(
                        BigDecimal.valueOf(boundingBox.get("minLat")),
                        BigDecimal.valueOf(boundingBox.get("maxLat")),
                        BigDecimal.valueOf(boundingBox.get("minLon")),
                        BigDecimal.valueOf(boundingBox.get("maxLon"))
                ).stream()
                .filter(address -> Objects.nonNull(address.getLatitude()) && Objects.nonNull(address.getLongitude()))
                .map(address -> new AbstractMap.SimpleEntry<>(
                        address,
                        calculateHaversineDistance(
                                latCenter, lonCenter,
                                address.getLatitude().doubleValue(),
                                address.getLongitude().doubleValue()
                        )
                ))
                .filter(entry -> entry.getValue() <= searchRadiusKm)
                .sorted(Comparator.comparingDouble(AbstractMap.Entry::getValue))
                .map(AbstractMap.Entry::getKey)
                .toList();
    }

    @Override
    public PropertyAddressModel save(PropertyAddressModel propertyAddressModel) {
        return propertyAddressDao.save(propertyAddressModel);
    }

    @Override
    public PropertyAddressModel update(Integer propertyId, PropertyAddressModel propertyAddressModel) {
        boolean makeChanges = false;
        PropertyAddressModel existingPropertyAddress = propertyAddressDao.findById(propertyId)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "Property with ID " + propertyId + " not found"
                ).get());

        if (propertyAddressModel.getStreet() != null) {
            existingPropertyAddress.setStreet(propertyAddressModel.getStreet());
            makeChanges = true;
        }

        if (propertyAddressModel.getNumber() != null) {
            existingPropertyAddress.setNumber(propertyAddressModel.getNumber());
            makeChanges = true;
        }

        if (propertyAddressModel.getFloor() != null) {
            existingPropertyAddress.setFloor(propertyAddressModel.getFloor());
            makeChanges = true;
        }

        if (propertyAddressModel.getDoor() != null) {
            existingPropertyAddress.setDoor(propertyAddressModel.getDoor());
            makeChanges = true;
        }

        if (propertyAddressModel.getPostalCode() != null) {
            existingPropertyAddress.setPostalCode(propertyAddressModel.getPostalCode());
            makeChanges = true;
        }

        if (propertyAddressModel.getCity() != null) {
            existingPropertyAddress.setCity(propertyAddressModel.getCity());
            makeChanges = true;
        }

        if (propertyAddressModel.getProvince() != null) {
            existingPropertyAddress.setProvince(propertyAddressModel.getProvince());
            makeChanges = true;
        }

        if (propertyAddressModel.getCountry() != null) {
            existingPropertyAddress.setCountry(propertyAddressModel.getCountry());
            makeChanges = true;
        }

        if (propertyAddressModel.getLatitude() != null) {
            existingPropertyAddress.setLatitude(propertyAddressModel.getLatitude());
            makeChanges = true;
        }

        if (propertyAddressModel.getLongitude() != null) {
            existingPropertyAddress.setLongitude(propertyAddressModel.getLongitude());
            makeChanges = true;
        }

        if (makeChanges) {
            return save(existingPropertyAddress);
        }

        return existingPropertyAddress;
    }

    private double calculateHaversineDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double latitudeRadiansDelta = Math.toRadians(latitude2 - latitude1);
        double longitudeRadiansDelta = Math.toRadians(longitude2 - longitude1);

        double haversineA = Math.sin(latitudeRadiansDelta / 2) * Math.sin(latitudeRadiansDelta / 2)
                + Math.cos(Math.toRadians(latitude1))
                * Math.cos(Math.toRadians(latitude2))
                * Math.sin(longitudeRadiansDelta / 2)
                * Math.sin(longitudeRadiansDelta / 2);

        double centralAngle = 2 * Math.atan2(Math.sqrt(haversineA), Math.sqrt(1 - haversineA));

        return EARTH_RADIUS_KM * centralAngle;
    }

    private Map<String, Double> calculateBoundingBox(double latitude, double longitude, double radiusKm) {
        double latitudeDelta = Math.toDegrees(radiusKm / EARTH_RADIUS_KM);

        double cosineLatitude = Math.cos(Math.toRadians(latitude));
        double longitudeDelta = (cosineLatitude < 1e-6) ? 180.0 : Math.toDegrees(radiusKm / (EARTH_RADIUS_KM * cosineLatitude));

        if (Double.isNaN(longitudeDelta) || Double.isInfinite(longitudeDelta)) {
            longitudeDelta = 180.0;
        }

        return Map.of(
                "minLat", Math.clamp(latitude - latitudeDelta, -90.0, 90.0),
                "maxLat", Math.clamp(latitude + latitudeDelta, -90.0, 90.0),
                "minLon", Math.clamp(longitude - longitudeDelta, -180.0, 180.0),
                "maxLon", Math.clamp(longitude + longitudeDelta, -180.0, 180.0)
        );
    }
}