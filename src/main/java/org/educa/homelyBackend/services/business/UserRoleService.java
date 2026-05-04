package org.educa.homelyBackend.services.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserRoleDao;
import org.educa.homelyBackend.models.UserRoleModel;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleDao userRoleDao;

    public Optional<UserRoleModel> findByName(String name) {
        return userRoleDao.findByName(name);
    }

    public UserRoleModel findByNameOrThrow(String name) {
        return findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "There is no user role with that name"
                ).get());
    }
}
