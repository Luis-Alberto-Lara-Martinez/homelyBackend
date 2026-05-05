package org.educa.homelyBackend.controllers.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.LocalLogInRequest;
import org.educa.homelyBackend.dtos.requests.LocalRegisterRequest;
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
    public ResponseEntity<Map<String, String>> localLogIn(@Valid @RequestBody LocalLogInRequest request) {
        return localAuthFacade.localLogIn(request);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> localRegister(@Valid @RequestBody LocalRegisterRequest request) {
        return localAuthFacade.localRegister(request);
    }
}