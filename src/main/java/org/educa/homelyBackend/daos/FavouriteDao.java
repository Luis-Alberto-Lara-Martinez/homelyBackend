package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.FavouriteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteDao extends JpaRepository<FavouriteModel, Integer> {
}
