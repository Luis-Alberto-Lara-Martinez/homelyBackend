package org.educa.homelyBackend.facades.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.request.LocalLogInRequest;
import org.educa.homelyBackend.dtos.request.LocalRegisterRequest;
import org.educa.homelyBackend.facades.LocalAuthFacade;
import org.educa.homelyBackend.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocalAuthFacadeImpl implements LocalAuthFacade {

    private final

    @Override
    public ResponseEntity<Map<String, String>> localLogIn(LocalLogInRequest request) {
        String email = request.email().toLowerCase();
        String password = request.password();

        UserModel user = userService.findByEmailOrThrow(email);

        userService.checkHashedPassword(password, user.getHashedPassword());

        return logInUtil.createResponse(user);
    }

    @Override
    public ResponseEntity<Map<String, String>> localRegister(LocalRegisterRequest request) {
        return null;
    }

    private
}
