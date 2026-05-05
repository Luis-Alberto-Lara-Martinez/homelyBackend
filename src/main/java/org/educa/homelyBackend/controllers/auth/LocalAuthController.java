package org.educa.homelyBackend.controllers.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.request.LocalLogInRequest;
import org.educa.homelyBackend.dtos.request.LocalRegisterRequest;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.impl.UserServiceImpl;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.LogInUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/local")
@RequiredArgsConstructor
public class LocalAuthController {

    private final UserServiceImpl userServiceImpl;
    private final LogInUtil logInUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> localLogIn(@Valid @RequestBody LocalLogInRequest request) {
        String email = request.email().toLowerCase();
        String password = request.password();

        UserModel user = userServiceImpl.findByEmailOrThrow(email);

        userServiceImpl.checkHashedPassword(password, user.getHashedPassword());

        return logInUtil.createResponse(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> localRegister(@Valid @RequestBody LocalRegisterRequest request) {
        String name = request.name().trim();
        String email = request.email().toLowerCase();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password and the confirmed password do not match").get();
        }

        return logInUtil.createResponse(userServiceImpl.createAndSendWelcomeEmail(name, email, password));
    }
}