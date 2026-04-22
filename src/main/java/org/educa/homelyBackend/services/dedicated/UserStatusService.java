package org.educa.homelyBackend.services.dedicated;

import jakarta.validation.constraints.NotBlank;
import org.educa.homelyBackend.daos.UserStatusDao;
import org.educa.homelyBackend.models.UserStatusModel;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class UserStatusService {

    private final UserStatusDao userStatusDao;

    public UserStatusService(UserStatusDao userStatusDao) {
        this.userStatusDao = userStatusDao;
    }

    private Optional<UserStatusModel> findByName(
            @NotBlank(message = "Name cannot be null nor empty")
            String name
    ) {
        return userStatusDao.findByName(name);
    }

    public UserStatusModel findByNameOrThrow(
            @NotBlank(message = "Name cannot be null nor empty")
            String name
    ) {
        return findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "There is no user with that name"
                ).get());
    }
}
