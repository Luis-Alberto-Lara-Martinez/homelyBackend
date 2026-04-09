package org.educa.homelyBackend.repository;

import org.educa.homelyBackend.entity.ResetPasswordTokens;
import org.educa.homelyBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResetPasswordTokensRepository extends JpaRepository<ResetPasswordTokens, Integer> {
    List<ResetPasswordTokens> findByIdUserOrderByCreatedAtDesc(Users idUser);

    Optional<ResetPasswordTokens> findFirstByIdUserOrderByCreatedAtDesc(Users idUser);
}