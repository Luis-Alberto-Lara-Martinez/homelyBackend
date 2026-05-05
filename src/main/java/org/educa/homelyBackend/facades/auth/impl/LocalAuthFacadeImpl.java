package org.educa.homelyBackend.facades.auth.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.request.LocalLogInRequest;
import org.educa.homelyBackend.dtos.request.LocalRegisterRequest;
import org.educa.homelyBackend.facades.auth.LocalAuthFacade;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.shared.PasswordEncoderService;
import org.educa.homelyBackend.services.shared.ResendService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.LogInUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocalAuthFacadeImpl implements LocalAuthFacade {

    private final UserService userService;
    private final PasswordEncoderService passwordEncoderService;
    private final ResendService resendService;
    private final LogInUtil logInUtil;

    @Override
    public ResponseEntity<Map<String, String>> localLogIn(LocalLogInRequest request) {
        String email = request.email().toLowerCase();
        String password = request.password();

        UserModel user = userService.findByEmailOrThrow(email);

        if (!passwordEncoderService.checkHashedPassword(password, user.getHashedPassword())) {
            throw ExceptionUtil.manageException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta").get();
        }

        return logInUtil.createResponse(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<Map<String, String>> localRegister(LocalRegisterRequest request) {
        String name = request.name().trim();
        String email = request.email().toLowerCase();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseña y su confirmación no coinciden"
            ).get();
        }

        return logInUtil.createResponse(logInUtil.createUserAndSendWelcomeEmail(
                name, email, password, "USER", "ACTIVE"
        ));
    }
}
