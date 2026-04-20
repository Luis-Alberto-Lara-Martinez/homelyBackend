package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.UserRoleDao;
import org.educa.homelyBackend.models.UserRoleModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleService {

    private final UserRoleDao userRolesRepository;

    public UserRoleService(UserRoleDao userRolesRepository) {
        this.userRolesRepository = userRolesRepository;
    }

    public Optional<UserRoleModel> findByName(String name) {
        return userRolesRepository.findByName(name);
    }
}