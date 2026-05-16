package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyAddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyAddressDao extends JpaRepository<PropertyAddressModel, Integer> {
    List<PropertyAddressModel> findAllByLatitudeBetweenAndLongitudeBetween(BigDecimal latitudeAfter, BigDecimal latitudeBefore, BigDecimal longitudeAfter, BigDecimal longitudeBefore);
}
