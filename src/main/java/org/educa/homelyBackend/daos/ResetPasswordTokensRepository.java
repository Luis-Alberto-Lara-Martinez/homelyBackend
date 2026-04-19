package org.educa.homelyBackend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResetPasswordTokensRepository extends JpaRepository<ResetPasswordTokens, Integer> {
    List<ResetPasswordTokens> findByIdUserOrderByCreatedAtDesc(Users idUser);

    Optional<ResetPasswordTokens> findFirstByIdUserOrderByCreatedAtDesc(Users idUser);
}