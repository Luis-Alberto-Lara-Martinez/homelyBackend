package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyAddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyAddressDao extends JpaRepository<PropertyAddressModel, Integer> {
}
