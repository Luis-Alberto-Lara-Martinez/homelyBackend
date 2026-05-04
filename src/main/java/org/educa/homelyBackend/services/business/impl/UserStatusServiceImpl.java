package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserStatusDao;
import org.educa.homelyBackend.models.UserStatusModel;
import org.educa.homelyBackend.services.business.UserStatusService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusDao userStatusDao;

    @Override
    public UserStatusModel findByNameOrThrow(String name) {
        return userStatusDao.findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No existe ningún userStatus con el name " + name
                ).get());
    }
}
