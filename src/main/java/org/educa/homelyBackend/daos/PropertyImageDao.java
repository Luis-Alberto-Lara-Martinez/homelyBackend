package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyImageModel;
import org.educa.homelyBackend.models.PropertyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyImageDao extends JpaRepository<PropertyImageModel, Integer> {
    List<PropertyImageModel> findAllByProperty(PropertyModel property);
}