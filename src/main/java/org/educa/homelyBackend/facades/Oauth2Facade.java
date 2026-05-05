package org.educa.homelyBackend.facades;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

public interface Oauth2Facade {
    ResponseEntity<Map<String, String>> oauth2LogIn(Jwt jwt);

    ResponseEntity<Map<String, String>> oauth2Register(Jwt jwt);
}
