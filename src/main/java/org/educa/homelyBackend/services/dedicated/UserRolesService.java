package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.UserRolesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRolesService {

    private final UserRolesRepository userRolesRepository;

    public UserRolesService(UserRolesRepository userRolesRepository) {
        this.userRolesRepository = userRolesRepository;
    }

    public Optional<UserRoles> findByName(String name) {
        return userRolesRepository.findByName(name);
    }
}