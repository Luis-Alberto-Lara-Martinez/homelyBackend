package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.FavouriteDao;
import org.educa.homelyBackend.models.FavouriteModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.FavouriteService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteDao favouriteDao;

    @Override
    public FavouriteModel findByUserOrThrow(UserModel user) {
        return favouriteDao.findByUser(user)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No se encontraron favoritos para el usuario con ID: " + user.getId()
                ).get());
    }

    @Override
    public FavouriteModel save(FavouriteModel favouriteModel) {
        return favouriteDao.save(favouriteModel);
    }

    @Override
    public void delete(FavouriteModel favouriteModel) {
        favouriteDao.delete(favouriteModel);
    }
}
