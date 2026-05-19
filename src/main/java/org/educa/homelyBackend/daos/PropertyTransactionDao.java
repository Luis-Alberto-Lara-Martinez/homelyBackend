package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyTransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyTransactionDao extends JpaRepository<PropertyTransactionModel, Integer> {
    Optional<PropertyTransactionModel> findByName(String name);
}