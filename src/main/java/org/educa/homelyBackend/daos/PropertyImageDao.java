package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyImageDao extends JpaRepository<PropertyImageModel, Integer> {
}
