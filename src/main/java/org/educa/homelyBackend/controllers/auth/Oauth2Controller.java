package org.educa.homelyBackend.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.LogInUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final UserService userService;
    private final LogInUtil logInUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> oauth2LogIn(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");

        if (email == null) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The token does not contain the claim 'email'").get();
        }

        return logInUtil.createResponse(userService.findByEmailOrThrow(email));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> oauth2Register(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        if (email == null) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The token does not contain the claim 'email'").get();
        }

        if (name == null) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The token does not contain the claim 'name'").get();
        }

        return logInUtil.createResponse(userService.createAndSendWelcomeEmail(name, email));
    }
}