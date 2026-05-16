package org.educa.homelyBackend.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.UserRoleResponse;
import org.educa.homelyBackend.facades.admin.UserRoleAdminFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.ADMIN)
@RequiredArgsConstructor
public class UserRoleAdminController {

    private final UserRoleAdminFacade userRoleAdminFacade;

    @GetMapping("/user/roles")
    public List<UserRoleResponse> getUserRoles() {
        return userRoleAdminFacade.findAll();
    }
}
