package org.educa.homelyBackend.facades.auth.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.facades.auth.Oauth2AuthFacade;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.LogInUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Oauth2AuthFacadeImpl implements Oauth2AuthFacade {

    private final UserService userService;
    private final LogInUtil logInUtil;

    @Override
    public ResponseEntity<Map<String, String>> oauth2LogIn(Jwt jwt) {
        String email = jwt.getClaim("email");

        if (email == null) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El token no contiene el claim 'email'"
            ).get();
        }

        return logInUtil.createResponse(userService.findByEmailOrThrow(email));
    }

    @Override
    public ResponseEntity<Map<String, String>> oauth2Register(Jwt jwt) {
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        if (email == null) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El token no contiene el claim 'email'"
            ).get();
        }

        if (name == null) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El token no contiene el claim 'name'"
            ).get();
        }

        return logInUtil.createResponse(logInUtil.createUserAndSendWelcomeEmail(
                name, email, null, "USER", "ACTIVE"
        ));
    }
}
