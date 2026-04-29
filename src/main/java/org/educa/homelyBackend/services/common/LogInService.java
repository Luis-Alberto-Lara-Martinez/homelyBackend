package org.educa.homelyBackend.services.common;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogInService {

    private final JwtService jwtService;

    public ResponseEntity<Map<String, String>> createLogInResponse(UserModel user) {
        return ResponseEntity.ok(Map.of(
                "message", "Successful login",
                "token", jwtService.generatePersonalizedJwt(user.getEmail(), user.getRole().getName())
        ));
    }
}
