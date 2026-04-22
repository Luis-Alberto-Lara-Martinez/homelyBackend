package org.educa.homelyBackend.services.dedicated;

import jakarta.validation.constraints.NotBlank;
import org.educa.homelyBackend.daos.UserRoleDao;
import org.educa.homelyBackend.models.UserRoleModel;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class UserRoleService {

    private final UserRoleDao userRoleDao;

    public UserRoleService(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    public Optional<UserRoleModel> findByName(
            @NotBlank(message = "Name cannot be null nor empty")
            String name
    ) {
        return userRoleDao.findByName(name);
    }

    public UserRoleModel findByNameOrThrow(
            @NotBlank(message = "Name cannot be null nor empty")
            String name
    ) {
        return findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "There is no user role with that name"
                ).get());
    }
}
