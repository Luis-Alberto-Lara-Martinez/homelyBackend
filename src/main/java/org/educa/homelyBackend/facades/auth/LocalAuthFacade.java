package org.educa.homelyBackend.facades.auth;

import org.educa.homelyBackend.dtos.requests.LocalLogInRequest;
import org.educa.homelyBackend.dtos.requests.LocalRegisterRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LocalAuthFacade {
    ResponseEntity<Map<String, String>> localLogIn(LocalLogInRequest request);

    ResponseEntity<Map<String, String>> localRegister(LocalRegisterRequest request);
}
