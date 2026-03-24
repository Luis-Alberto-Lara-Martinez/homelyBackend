package org.educa.homelyBackend.service.domain;

import org.educa.homelyBackend.entity.UserRoles;
import org.educa.homelyBackend.repository.UserRolesRepository;
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