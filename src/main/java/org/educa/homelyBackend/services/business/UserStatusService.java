package org.educa.homelyBackend.services.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserStatusDao;
import org.educa.homelyBackend.models.UserStatusModel;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusDao userStatusDao;

    private Optional<UserStatusModel> findByName(String name) {
        return userStatusDao.findByName(name);
    }

    public UserStatusModel findByNameOrThrow(String name) {
        return findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "There is no user with that name"
                ).get());
    }
}
