package org.educa.homelyBackend.facades.auth;

import org.educa.homelyBackend.dtos.requests.LogInDtoRequest;
import org.educa.homelyBackend.dtos.requests.RegisterDtoRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LocalAuthFacade {

    ResponseEntity<Map<String, String>> logIn(LogInDtoRequest request);

    ResponseEntity<Map<String, String>> register(RegisterDtoRequest request);
}
