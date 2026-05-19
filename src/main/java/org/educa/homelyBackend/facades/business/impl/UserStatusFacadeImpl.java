package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.UserStatusDto;
import org.educa.homelyBackend.facades.business.UserStatusFacade;
import org.educa.homelyBackend.services.business.UserStatusService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStatusFacadeImpl implements UserStatusFacade {

    private final UserStatusService userStatusService;

    @Override
    public List<UserStatusDto> findAll() {
        return userStatusService.findAll().stream()
                .map(userStatus -> UserStatusDto.builder()
                        .id(userStatus.getId())
                        .name(userStatus.getName())
                        .build())
                .toList();
    }
}


