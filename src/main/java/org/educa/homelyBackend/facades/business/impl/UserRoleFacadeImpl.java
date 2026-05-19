package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.UserRoleDto;
import org.educa.homelyBackend.facades.business.UserRoleFacade;
import org.educa.homelyBackend.services.business.UserRoleService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRoleFacadeImpl implements UserRoleFacade {

    private final UserRoleService userRoleService;

    @Override
    public List<UserRoleDto> findAll() {
        return userRoleService.findAll().stream()
                .map(userRole -> UserRoleDto.builder()
                        .id(userRole.getId())
                        .name(userRole.getName())
                        .build())
                .toList();
    }
}


