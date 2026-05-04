package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserRoleDao;
import org.educa.homelyBackend.models.UserRoleModel;
import org.educa.homelyBackend.services.business.UserRoleService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleDao userRoleDao;

    @Override
    public UserRoleModel findByNameOrThrow(String name) {
        return userRoleDao.findByName(name)
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No existe ningún userStatus con el name " + name
                ).get());
    }
}
