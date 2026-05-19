package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyStatusDao extends JpaRepository<PropertyStatusModel, Integer> {
    Optional<PropertyStatusModel> findByName(String name);
}