package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyStatusDao extends JpaRepository<PropertyStatusModel, Integer> {
}
