package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleDao extends JpaRepository<UserRoleModel, Integer> {
    Optional<UserRoleModel> findByName(String name);
}
