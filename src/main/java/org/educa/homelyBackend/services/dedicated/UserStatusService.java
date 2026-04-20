package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.UserStatusesRepository;
import org.educa.homelyBackend.models.UserStatusModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserStatusService {

    private final UserStatusesRepository userStatusesRepository;

    public UserStatusService(UserStatusesRepository userStatusesRepository) {
        this.userStatusesRepository = userStatusesRepository;
    }

    public Optional<UserStatusModel> findByName(String name) {
        return userStatusesRepository.findByName(name);
    }
}