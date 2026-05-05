package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserDao;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserService;
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
    public UserModel saveOrUpdate(UserModel user) {
        return userDao.save(user);
    }

    @Override
    public UserModel updateHashedPassword(UserModel user, String password) {
        user.setHashedPassword(passwordEncoderService.generateHashedPassword(password));
        return saveOrUpdate(user);
    }

    @Override
    public UserModel updateImage(UserModel user, MultipartFile avatarFile) {
        user.setImageUrl(cloudinaryService.uploadAvatarImage(avatarFile, user.getId()));
        return saveOrUpdate(user);
    }

    @Override
    public UserModel updateName(UserModel user, String name) {
        user.setName(name);
        return saveOrUpdate(user);
    }
}
