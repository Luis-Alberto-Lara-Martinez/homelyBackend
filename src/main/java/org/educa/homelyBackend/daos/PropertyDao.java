package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyDao extends JpaRepository<PropertyModel, Integer> {
}
