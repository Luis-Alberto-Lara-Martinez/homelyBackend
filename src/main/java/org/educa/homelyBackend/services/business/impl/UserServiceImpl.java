package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserDao;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public Page<UserModel> findAll(Integer pageNumber, Integer pageSize) {
        Page<UserModel> pagedUsers = userDao.findAll(PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").ascending()));

        if (pagedUsers.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No existe ningún usuario").get();
        }

        return pagedUsers;
    }

    @Override
    public UserModel findByEmailOrThrow(String email) {
        return userDao.findByEmail(email).orElseThrow(() -> ExceptionUtil.manageException(
                HttpStatus.NOT_FOUND,
                "No existe ningún usuario con ese correo electrónico"
        ).get());
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public UserModel save(UserModel user) {
        return userDao.save(user);
    }

    @Override
    public UserModel update(String email, UserModel user) {
        return update(findByEmailOrThrow(email), user);
    }

    @Override
    public UserModel update(UserModel user, UserModel updatedUser) {
        boolean makeChanges = false;

        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
            makeChanges = true;
        }

        if (updatedUser.getStatus() != null) {
            user.setStatus(updatedUser.getStatus());
            makeChanges = true;
        }

        if (updatedUser.getImageUrl() != null) {
            user.setImageUrl(updatedUser.getImageUrl());
            makeChanges = true;
        }

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
            makeChanges = true;
        }

        if (updatedUser.getHashedPassword() != null) {
            user.setHashedPassword(updatedUser.getHashedPassword());
            makeChanges = true;
        }

        if (updatedUser.getUpdatedBy() != null) {
            user.setUpdatedBy(updatedUser.getUpdatedBy());
            makeChanges = true;
        }

        if (makeChanges) {
            return save(user);
        }

        return user;
    }

    @Override
    public void delete(UserModel user) {
        userDao.delete(user);
    }
}
