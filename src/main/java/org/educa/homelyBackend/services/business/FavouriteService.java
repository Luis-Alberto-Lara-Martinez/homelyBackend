package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.FavouriteModel;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.models.UserModel;

import java.util.List;

public interface FavouriteService {
    List<FavouriteModel> findAllByUser(UserModel user);

    FavouriteModel save(FavouriteModel favouriteModel);

    void deleteByUserAndProperty(UserModel user, PropertyModel property);
}
