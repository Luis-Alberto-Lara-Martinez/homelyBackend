package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.UserModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    Page<UserModel> findAll(Integer page, Integer size);

    UserModel findByEmailOrThrow(String email);

    Optional<UserModel> findByEmail(String email);

    UserModel save(UserModel user);

    UserModel update(String email, UserModel user);

    UserModel update(UserModel user, UserModel updatedUser);

    void delete(UserModel user);
}
