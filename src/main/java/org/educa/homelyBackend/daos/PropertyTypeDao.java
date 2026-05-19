package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyTypeDao extends JpaRepository<PropertyTypeModel, Integer> {
    Optional<PropertyTypeModel> findByName(String name);
}