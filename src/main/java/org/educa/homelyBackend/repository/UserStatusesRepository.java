package org.educa.homelyBackend.repository;

import org.educa.homelyBackend.entity.UserStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusesRepository extends JpaRepository<UserStatuses, Integer> {
    Optional<UserStatuses> findByName(String name);
}