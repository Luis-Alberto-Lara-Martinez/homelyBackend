package org.educa.homelyBackend.facades.admin.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.UserStatusResponse;
import org.educa.homelyBackend.facades.admin.UserStatusAdminFacade;
import org.educa.homelyBackend.services.business.UserStatusService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStatusAdminFacadeImpl implements UserStatusAdminFacade {

    private final UserStatusService userStatusService;

    @Override
    public List<UserStatusResponse> findAll() {
        return userStatusService.findAll().stream()
                .map(userStatus -> UserStatusResponse.builder()
                        .id(userStatus.getId())
                        .name(userStatus.getName())
                        .build())
                .toList();
    }
}


