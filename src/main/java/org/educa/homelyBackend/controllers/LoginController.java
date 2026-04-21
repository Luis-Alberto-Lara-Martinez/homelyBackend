package org.educa.homelyBackend.controllers;

import jakarta.validation.Valid;
import org.educa.homelyBackend.dtos.LoginTraditionalRequest;
import org.educa.homelyBackend.dtos.RegisterTraditionalRequest;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.common.EncoderService;
import org.educa.homelyBackend.services.dedicated.UserService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    private final UserService userService;
    private final EncoderService encoderService;

    public LoginController(UserService userService, EncoderService encoderService) {
        this.userService = userService;
        this.encoderService = encoderService;
    }

    @PostMapping("/oauth2/login")
    public ResponseEntity<Map<String, String>> oauth2LogIn(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");

        if (email == null) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The token does not contain the claim 'email'").get();
        }

        UserModel user = userService.findByEmail(email);

        return createLogInResponse(user);
    }

    @PostMapping("/oauth2/register")
    public ResponseEntity<Map<String, String>> oauth2Register(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        if (email == null) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The token does not contain the claim 'email'").get();
        }

        if (name == null) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The token does not contain the claim 'name'").get();
        }

        UserModel user = userService.createAndSendWelcomeEmail(name, email);

        return createLogInResponse(user);
    }

    @PostMapping("/local/login")
    public ResponseEntity<Map<String, String>> localLogIn(@Valid @RequestBody LoginTraditionalRequest request) {
        String email = request.email().toLowerCase();
        String password = request.password();

        UserModel user = userService.findByEmail(email);

        if (encoderService.checkHashPassword(password, user.getHashedPassword())) {
            return createLogInResponse(user);
        } else {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password is incorrect").get();
        }
    }

    @PostMapping("/local/register")
    public ResponseEntity<Map<String, String>> localRegister(@Valid @RequestBody RegisterTraditionalRequest request) {
        String name = request.name().trim();
        String email = request.email().toLowerCase();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password and the confirmed password do not match").get();
        }

        UserModel user = userService.createAndSendWelcomeEmail(name, email, password);

        return createLogInResponse(user);
    }

    private ResponseEntity<Map<String, String>> createLogInResponse(UserModel user) {
        String jwt = encoderService.generatePersonalizedJwt(user.getEmail(), Map.of(
                "name", user.getName(),
                "role", user.getRole().getName()
        ));

        return ResponseEntity.ok(Map.of(
                "message", "Successful login",
                "token", jwt
        ));
    }
}