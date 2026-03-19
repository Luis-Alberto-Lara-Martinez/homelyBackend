package org.educa.homelyBackend.repository;

import org.educa.homelyBackend.entity.Users;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Integer> {
    @NonNull List<Users> findAll();
}