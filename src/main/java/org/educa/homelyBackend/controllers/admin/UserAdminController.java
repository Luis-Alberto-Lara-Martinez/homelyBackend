package org.educa.homelyBackend.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.services.business.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(userService.findAll(1, 10));
    }
}