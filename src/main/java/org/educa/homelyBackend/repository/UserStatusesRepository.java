package org.educa.homelyBackend.repository;

import org.educa.homelyBackend.entity.UserStatuses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatusesRepository extends JpaRepository<UserStatuses, Integer> {
    Optional<UserStatuses> findByName(String name);
}