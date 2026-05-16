package org.educa.homelyBackend.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.UserStatusResponse;
import org.educa.homelyBackend.facades.admin.UserStatusAdminFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.ADMIN)
@RequiredArgsConstructor
public class UserStatusAdminController {

    private final UserStatusAdminFacade userStatusAdminFacade;

    @GetMapping("/user/statuses")
    public List<UserStatusResponse> getUserStatuses() {
        return userStatusAdminFacade.findAll();
    }
}
