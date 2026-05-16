package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.UserStatusModel;

import java.util.List;

public interface UserStatusService {
    UserStatusModel findByNameOrThrow(String name);

    List<UserStatusModel> findAll();
}
