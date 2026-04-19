package org.educa.homelyBackend.controllers;

import org.educa.homelyBackend.services.dedicated.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class UsersAdminController extends BaseController {

    // TODO: hacer archivo entero

    private final UsersService usersService;

    public UsersAdminController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok().build();
    }
}