package org.educa.homelyBackend.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.request.CheckResetTokenRequest;
import org.educa.homelyBackend.dtos.request.ForgottenPasswordRequest;
import org.educa.homelyBackend.dtos.request.ResetPasswordRequest;
import org.educa.homelyBackend.services.business.ResetTokenServiceImpl;
import org.educa.homelyBackend.services.business.impl.UserServiceImpl;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResetTokenApiController {

    private final ResetTokenServiceImpl resetTokenServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/forgotten-password")
    public ResponseEntity<Map<String, String>> forgottenPassword(@Valid @RequestBody ForgottenPasswordRequest request) {
        String email = request.email().toLowerCase();

        resetTokenServiceImpl.createAndSendResetEmail(userServiceImpl.findByEmailOrThrow(email));

        return ResponseEntityUtil.ok("Email de restablecimiento enviado correctamente");
    }

    @GetMapping("/check-reset-token")
    public ResponseEntity<Map<String, String>> checkResetToken(@Valid @RequestBody CheckResetTokenRequest request) {
        String token = request.token();

        resetTokenServiceImpl.checkTokenAndUpdateIfExpired(resetTokenServiceImpl.findByTokenOrThrow(token));

        return ResponseEntityUtil.ok("Token válido");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String token = request.token();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden").get();
        }

        resetTokenServiceImpl.updateUserAndUsed(resetTokenServiceImpl.findByTokenOrThrow(token), password);

        return ResponseEntityUtil.ok("Contraseña restablecida correctamente");
    }
}