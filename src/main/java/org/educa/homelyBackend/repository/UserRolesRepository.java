package org.educa.homelyBackend.repository;

import org.educa.homelyBackend.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Integer> {
    Optional<UserRoles> findByName(String name);
}