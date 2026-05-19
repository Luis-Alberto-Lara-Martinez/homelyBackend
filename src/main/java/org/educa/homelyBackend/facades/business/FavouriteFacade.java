package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.FavouriteDto;

import java.util.List;

public interface FavouriteFacade {
    void save(String email, Integer propertyId);

    void delete(String email, Integer propertyId);

    List<FavouriteDto> findAllByUser(String email);
}
