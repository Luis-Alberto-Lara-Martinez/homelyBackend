package org.educa.homelyBackend.repository;

import org.educa.homelyBackend.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRolesRepository extends JpaRepository<UserRoles, Integer> {
    Optional<UserRoles> findByName(String name);
}