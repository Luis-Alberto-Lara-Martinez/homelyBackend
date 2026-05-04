package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.UserRoleModel;

public interface UserRoleService {
    UserRoleModel findByNameOrThrow(String name);
}
