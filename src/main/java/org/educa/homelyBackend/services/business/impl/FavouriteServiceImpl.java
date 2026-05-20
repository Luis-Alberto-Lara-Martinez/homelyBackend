package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.FavouriteDao;
import org.educa.homelyBackend.models.FavouriteModel;
import org.educa.homelyBackend.models.PropertyModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.FavouriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteDao favouriteDao;

    @Override
    public List<FavouriteModel> findAllByUser(UserModel user) {
        return favouriteDao.findAllByUser(user);
    }

    @Override
    public FavouriteModel save(FavouriteModel favouriteModel) {
        return favouriteDao.save(favouriteModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserAndProperty(UserModel user, PropertyModel property) {
        favouriteDao.deleteByUserAndProperty(user, property);
    }
}
