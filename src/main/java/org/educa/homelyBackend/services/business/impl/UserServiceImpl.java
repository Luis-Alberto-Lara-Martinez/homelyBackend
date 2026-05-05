package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserDao;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserRoleService;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.business.UserStatusService;
import org.educa.homelyBackend.services.shared.AvatarService;
import org.educa.homelyBackend.services.shared.CloudinaryService;
import org.educa.homelyBackend.services.shared.PasswordEncoderService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoderService passwordEncoderService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;

    @Override
    public Page<UserModel> findAll(Integer pageNumber, Integer pageSize, String sortBy) {
        if (pageNumber == null || pageNumber - 1 < 0) {
            pageNumber = 0;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = 30;
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }

        Page<UserModel> pagedUsers = userDao.findAll(PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortBy).ascending()));

        if (pagedUsers.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No existe ningún usuario").get();
        }

        return pagedUsers;
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public UserModel findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(() -> ExceptionUtil.manageException(
                HttpStatus.NOT_FOUND,
                "No existe ningún usuario con ese correo electrónico"
        ).get());
    }

    @Override
    public UserModel createUser(String email, String name, String password, String role, String status) {
        if (findByEmail(email).isPresent()) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El correo electrónico ya está registrado"
            ).get();
        }

        UserModel user = UserModel.builder()
                .role(userRoleService.findByNameOrThrow(role))
                .status(userStatusService.findByNameOrThrow(status))
                .name(name)
                .email(email)
                .build();

        if (password != null && !password.isBlank()) {
            user = updateHashedPassword(user, password);
        }

        return save(updateImage(save(user), avatarService.generateAvatar(name)));
    }

    @Override
    public UserModel save(UserModel user) {
        return userDao.save(user);
    }

    @Override
    public UserModel updateHashedPassword(UserModel user, String password) {
        user.setHashedPassword(passwordEncoderService.generateHashedPassword(password));
        return save(user);
    }

    @Override
    public UserModel updateImage(UserModel user, MultipartFile avatarFile) {
        user.setImageUrl(cloudinaryService.uploadAvatarImage(avatarFile, user.getId()));
        return save(user);
    }

    @Override
    public UserModel updateImage(UserModel user, byte[] rawAvatarFile) {
        user.setImageUrl(cloudinaryService.uploadAvatarImage(rawAvatarFile, user.getId()));
        return save(user);
    }

    @Override
    public UserModel updateName(UserModel user, String name) {
        user.setName(name);
        return save(user);
    }
}
