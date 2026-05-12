package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    Page<UserModel> findAll(Integer page, Integer size, String sortBy);

    Optional<UserModel> findByEmail(String email);

    UserModel findByEmailOrThrow(String email);

    UserModel createUser(String email, String name, String password, String role, String status);

    UserModel save(UserModel user);

    UserModel updateHashedPassword(UserModel user, String password);

    UserModel updateImage(UserModel user, MultipartFile avatarFile);

    UserModel updateImage(UserModel user, byte[] rawAvatarFile);

    UserModel updateName(UserModel user, String name);
}
