package org.educa.homelyBackend.controllers.admin;

import org.educa.homelyBackend.services.dedicated.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(userService.findAll(1, 10));
    }
}