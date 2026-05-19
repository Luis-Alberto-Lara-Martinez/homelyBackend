package org.educa.homelyBackend.controllers.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.UserRoleDto;
import org.educa.homelyBackend.facades.business.UserRoleFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.ADMIN + "/user/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleFacade userRoleFacade;

    @GetMapping("")
    public List<UserRoleDto> getUserRoles() {
        return userRoleFacade.findAll();
    }
}
