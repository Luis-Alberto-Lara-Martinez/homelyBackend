package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.UserModel;
import org.springframework.data.domain.Page;

public interface PropertyService {
    Page<UserModel> findAll(Integer page, Integer size, String sortBy);


}
