package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.UserRoleModel;

import java.util.List;

public interface UserRoleService {
    UserRoleModel findByNameOrThrow(String name);

    List<UserRoleModel> findAll();
}
