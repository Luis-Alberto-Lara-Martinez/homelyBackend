package org.educa.homelyBackend.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.facades.Oauth2Facade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class Oauth2AuthController {

    private final Oauth2Facade oauth2Facade;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> oauth2LogIn(@AuthenticationPrincipal Jwt jwt) {
        return oauth2Facade.oauth2LogIn(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> oauth2Register(@AuthenticationPrincipal Jwt jwt) {
        return oauth2Facade.oauth2Register(jwt);
    }
}