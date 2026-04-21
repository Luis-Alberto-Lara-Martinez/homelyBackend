package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.UserStatusesRepository;
import org.educa.homelyBackend.models.UserStatusModel;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserStatusService {

    private final UserStatusesRepository userStatusesRepository;

    public UserStatusService(UserStatusesRepository userStatusesRepository) {
        this.userStatusesRepository = userStatusesRepository;
    }

    public UserStatusModel findByName(String name) {
        return userStatusesRepository
                .findByName(name)
                .orElseThrow(ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "User status not found with name: " + name));
    }
}
