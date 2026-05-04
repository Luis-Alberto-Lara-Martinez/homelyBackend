package org.educa.homelyBackend.utils;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.common.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LogInUtil {

    private final JwtService jwtService;

    public ResponseEntity<Map<String, String>> createResponse(UserModel user) {
        return ResponseEntity.ok(Map.of(
                "message", "Inicio de sesión exitoso",
                "token", jwtService.generatePersonalizedJwt(user.getEmail(), user.getRole().getName())
        ));
    }
}
