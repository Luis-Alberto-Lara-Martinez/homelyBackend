package org.educa.homelyBackend.facades.admin.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.UserRoleResponse;
import org.educa.homelyBackend.facades.admin.UserRoleAdminFacade;
import org.educa.homelyBackend.services.business.UserRoleService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRoleAdminFacadeImpl implements UserRoleAdminFacade {

    private final UserRoleService userRoleService;

    @Override
    public List<UserRoleResponse> findAll() {
        return userRoleService.findAll().stream()
                .map(userRole -> UserRoleResponse.builder()
                        .id(userRole.getId())
                        .name(userRole.getName())
                        .build())
                .toList();
    }
}


