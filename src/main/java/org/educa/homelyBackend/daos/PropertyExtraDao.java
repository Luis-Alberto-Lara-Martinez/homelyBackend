package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyExtraModel;
import org.educa.homelyBackend.models.PropertyTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PropertyExtraDao extends JpaRepository<PropertyExtraModel, Integer> {
    Optional<PropertyExtraModel> findByName(String name);

    List<PropertyExtraModel> findByPropertyTypes(Set<PropertyTypeModel> propertyTypes);
}