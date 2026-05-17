package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.PropertyAddressDao;
import org.educa.homelyBackend.models.PropertyAddressModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.PropertyAddressService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyAddressServiceImpl implements PropertyAddressService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final PropertyAddressDao propertyAddressDao;

    @Override
    public List<PropertyAddressModel> findAddressesWithinRadius(double latitude, double longitude, Integer radiusKm) {
        if (radiusKm == null || radiusKm <= 0) {
            return List.of();
        }

        double radius = radiusKm.doubleValue();
        BoundingBox boundingBox = buildBoundingBox(latitude, longitude, radius);

        return propertyAddressDao.findAllByLatitudeBetweenAndLongitudeBetween(
                        BigDecimal.valueOf(boundingBox.minLatitude()),
                        BigDecimal.valueOf(boundingBox.maxLatitude()),
                        BigDecimal.valueOf(boundingBox.minLongitude()),
                        BigDecimal.valueOf(boundingBox.maxLongitude())
                ).stream()
                .filter(address -> isWithinRadius(latitude, longitude, address, radius))
                .sorted(Comparator.comparingDouble(address -> distanceKm(
                        latitude,
                        longitude,
                        address.getLatitude().doubleValue(),
                        address.getLongitude().doubleValue()
                )))
                .toList();
    }

    @Override
    public Page<PropertyAddressModel> findAll(Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageNumber - 1 < 0) {
            pageNumber = 0;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = 30;
        }

        Page<PropertyAddressModel> pagedPropertyAddresses = propertyAddressDao.findAll(PageRequest.of(pageNumber - 1, pageSize));

        if (pagedPropertyAddresses.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No existe ningún usuario").get();
        }

        return pagedPropertyAddresses;
    }

    @Override
    public PropertyAddressModel save(PropertyAddressModel propertyAddressModel) {
        return propertyAddressDao.save(propertyAddressModel);
    }

    private boolean isWithinRadius(double centerLatitude, double centerLongitude, PropertyAddressModel address, double radiusKm) {
        if (address.getLatitude() == null || address.getLongitude() == null) {
            return false;
        }

        return distanceKm(
                centerLatitude,
                centerLongitude,
                address.getLatitude().doubleValue(),
                address.getLongitude().doubleValue()
        ) <= radiusKm;
    }

    private double distanceKm(double latitude1, double longitude1, double latitude2, double longitude2) {
        double lat1Rad = Math.toRadians(latitude1);
        double lat2Rad = Math.toRadians(latitude2);
        double deltaLat = Math.toRadians(latitude2 - latitude1);
        double deltaLon = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private BoundingBox buildBoundingBox(double latitude, double longitude, double radiusKm) {
        double latitudeDelta = Math.toDegrees(radiusKm / EARTH_RADIUS_KM);
        double minLatitude = clamp(latitude - latitudeDelta, -90.0, 90.0);
        double maxLatitude = clamp(latitude + latitudeDelta, -90.0, 90.0);

        double longitudeDelta;
        if (Math.abs(latitude) >= 90.0) {
            longitudeDelta = 180.0;
        } else {
            longitudeDelta = Math.toDegrees(radiusKm / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(latitude))));
            if (Double.isNaN(longitudeDelta) || Double.isInfinite(longitudeDelta)) {
                longitudeDelta = 180.0;
            }
        }

        double minLongitude = clamp(longitude - longitudeDelta, -180.0, 180.0);
        double maxLongitude = clamp(longitude + longitudeDelta, -180.0, 180.0);

        return new BoundingBox(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private record BoundingBox(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
    }
}
