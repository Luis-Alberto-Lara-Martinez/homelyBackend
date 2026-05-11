package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyExtraModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyExtraDao extends JpaRepository<PropertyExtraModel, Integer> {
}
