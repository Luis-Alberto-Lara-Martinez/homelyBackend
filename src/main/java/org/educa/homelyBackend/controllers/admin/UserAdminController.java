package org.educa.homelyBackend.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.services.business.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(userServiceImpl.findAll(1, 10));
    }
}