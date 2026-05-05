package org.educa.homelyBackend.facades;

import org.educa.homelyBackend.dtos.request.LocalLogInRequest;
import org.educa.homelyBackend.dtos.request.LocalRegisterRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LocalAuthFacade {
    ResponseEntity<Map<String, String>> localLogIn(LocalLogInRequest request);

    ResponseEntity<Map<String, String>> localRegister(LocalRegisterRequest request);
}
