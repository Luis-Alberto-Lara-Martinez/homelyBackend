package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.UserRoleDao;
import org.educa.homelyBackend.models.UserRoleModel;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private final UserRoleDao userRolesRepository;

    public UserRoleService(UserRoleDao userRolesRepository) {
        this.userRolesRepository = userRolesRepository;
    }

    public UserRoleModel findByName(String name) {
        return userRolesRepository
                .findByName(name)
                .orElseThrow(ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "User role not found with name: " + name));
    }
}
