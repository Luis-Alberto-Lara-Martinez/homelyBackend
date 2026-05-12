package org.educa.homelyBackend.services.business;

import org.educa.homelyBackend.models.UserStatusModel;

public interface UserStatusService {
    UserStatusModel findByNameOrThrow(String name);
}
