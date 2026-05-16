package org.educa.homelyBackend.facades.admin;

import org.educa.homelyBackend.dtos.responses.UserRoleResponse;

import java.util.List;

public interface UserRoleAdminFacade {
    List<UserRoleResponse> findAll();
}
