package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.UserRoleDto;

import java.util.List;

public interface UserRoleFacade {
    List<UserRoleDto> findAll();
}
