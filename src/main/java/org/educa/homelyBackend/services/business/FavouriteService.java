package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.FavouriteModel;

public interface FavouriteService {
    FavouriteModel findByUserId(Integer userId);

    FavouriteModel save(FavouriteModel favouriteModel);

    void delete(FavouriteModel favouriteModel);
}
