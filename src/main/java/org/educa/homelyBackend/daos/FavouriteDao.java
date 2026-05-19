package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.FavouriteModel;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteDao extends JpaRepository<FavouriteModel, Integer> {
    List<FavouriteModel> findAllByUser(UserModel user);

    void deleteByUserAndProperty(UserModel user, PropertyModel property);
}
