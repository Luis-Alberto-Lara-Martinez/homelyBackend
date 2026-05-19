package org.educa.homelyBackend.controllers.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.LogInDtoRequest;
import org.educa.homelyBackend.dtos.requests.RegisterDtoRequest;
import org.educa.homelyBackend.facades.auth.LocalAuthFacade;
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

    private final LocalAuthFacade localAuthFacade;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> localLogIn(@Valid @RequestBody LogInDtoRequest request) {
        return localAuthFacade.logIn(request);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> localRegister(@Valid @RequestBody RegisterDtoRequest request) {
        return localAuthFacade.register(request);
    }
}