package org.educa.homelyBackend.utils;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.shared.JwtService;
import org.educa.homelyBackend.services.shared.ResendService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LogInUtil {

    private final UserService userService;
    private final ResendService resendService;
    private final JwtService jwtService;

    @Transactional(rollbackFor = Exception.class)
    public UserModel createUserAndSendWelcomeEmail(
            String name, String email, String password, String role, String status
    ) {
        resendService.sendWelcomeEmail(email, name);

        return userService.createUser(email, name, password, role, status);
    }

    public ResponseEntity<Map<String, String>> createResponse(UserModel user) {
        return ResponseEntity.ok(Map.of(
                "message", "Inicio de sesión exitoso",
                "token", jwtService.generatePersonalizedJwt(user.getEmail(), user.getRole().getName())
        ));
    }
}
