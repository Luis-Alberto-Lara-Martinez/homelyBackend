package org.educa.homelyBackend.facades.admin;

import org.educa.homelyBackend.dtos.responses.UserStatusResponse;

import java.util.List;

public interface UserStatusAdminFacade {
    List<UserStatusResponse> findAll();
}
