package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.UserStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusDao extends JpaRepository<UserStatusModel, Integer> {
    Optional<UserStatusModel> findByName(String name);
}
