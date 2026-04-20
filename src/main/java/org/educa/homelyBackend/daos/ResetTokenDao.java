package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.ResetTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenDao extends JpaRepository<ResetTokenModel, Integer> {
    Optional<ResetTokenModel> findByHashedToken(String hashedToken);
}
