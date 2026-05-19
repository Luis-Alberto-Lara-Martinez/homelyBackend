package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.FavouriteModel;
import org.educa.homelyBackend.models.UserModel;

public interface FavouriteService {
    FavouriteModel findByUserOrThrow(UserModel user);

    FavouriteModel save(FavouriteModel favouriteModel);

    void delete(FavouriteModel favouriteModel);
}
