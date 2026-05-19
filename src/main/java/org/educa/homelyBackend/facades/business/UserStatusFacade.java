package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.UserStatusDto;

import java.util.List;

public interface UserStatusFacade {
    List<UserStatusDto> findAll();
}
