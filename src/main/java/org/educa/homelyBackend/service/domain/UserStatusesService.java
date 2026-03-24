package org.educa.homelyBackend.service.domain;

import org.educa.homelyBackend.entity.UserStatuses;
import org.educa.homelyBackend.repository.UserStatusesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserStatusesService {

    private final UserStatusesRepository userStatusesRepository;

    public UserStatusesService(UserStatusesRepository userStatusesRepository) {
        this.userStatusesRepository = userStatusesRepository;
    }

    public Optional<UserStatuses> findByName(String name) {
        return userStatusesRepository.findByName(name);
    }
}