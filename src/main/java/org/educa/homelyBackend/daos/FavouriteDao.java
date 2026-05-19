package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.FavouriteModel;
import org.educa.homelyBackend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteDao extends JpaRepository<FavouriteModel, Integer> {
    Optional<FavouriteModel> findByUser(UserModel user);
}
