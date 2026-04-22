package org.educa.homelyBackend.services.common;

import org.educa.homelyBackend.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Service
@Validated
public class LogInService {

    private final EncoderService encoderService;

    public LogInService(EncoderService encoderService) {
        this.encoderService = encoderService;
    }

    public ResponseEntity<Map<String, String>> createLogInResponse(UserModel user) {
        String jwt = encoderService.generatePersonalizedJwt(user.getEmail(), Map.of(
                "name", user.getName(),
                "role", user.getRole().getName()
        ));

        return ResponseEntity.ok(Map.of(
                "message", "Successful login",
                "token", jwt
        ));
    }
}
