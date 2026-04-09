package org.educa.homelyBackend.repository;

import org.educa.homelyBackend.entity.ResetPasswordTokens;
import org.educa.homelyBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResetPasswordTokensRepository extends JpaRepository<ResetPasswordTokens, Integer> {
    List<ResetPasswordTokens> findByIdUser(Users idUser);
}