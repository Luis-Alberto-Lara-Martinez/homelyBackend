package org.educa.homelyBackend.controllers.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.UserStatusDto;
import org.educa.homelyBackend.facades.business.UserStatusFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ConfigurationRoutes.ADMIN + "/user/statuses")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusFacade userStatusFacade;

    @GetMapping("/user/statuses")
    public List<UserStatusDto> getUserStatuses() {
        return userStatusFacade.findAll();
    }
}
