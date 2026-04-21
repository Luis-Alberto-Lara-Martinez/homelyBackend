package org.educa.homelyBackend.controllers;

import jakarta.validation.Valid;
import org.educa.homelyBackend.dtos.CheckResetTokenRequest;
import org.educa.homelyBackend.dtos.ForgetUserPasswordRequest;
import org.educa.homelyBackend.dtos.ResetUserPasswordRequest;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.services.common.EmailService;
import org.educa.homelyBackend.services.dedicated.ResetTokenService;
import org.educa.homelyBackend.services.dedicated.UserService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ResetTokenController {

    // TODO: revisar archivo entero

    private final ResetTokenService resetTokenService;
    private final UserService userService;
    private final EmailService emailService;

    public ResetTokenController(ResetTokenService resetTokenService, UserService userService, EmailService emailService) {
        this.resetTokenService = resetTokenService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/forgotten-password")
    public ResponseEntity<Map<String, String>> forgottenPassword(@Valid @RequestBody ForgetUserPasswordRequest request) {
        String email = request.email().toLowerCase();

        if (!userService.existsByEmail(email)) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No user was found with the provided email address").get();
        }

        resetTokenService.createAndSendResetEmail(userService.findByEmail(email));

        return ResponseEntity.ok(Map.of(
                "message", "An email has been sent to reset the password"
        ));
    }

    @GetMapping("/check-reset-token")
    public ResponseEntity<Map<String, String>> checkResetToken(@Valid @RequestBody CheckResetTokenRequest request) {
        String token = request.token();

        if (!resetTokenService.existsByToken(token)) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No reset token found for the provided token").get();
        }

        ResetTokenModel resetToken = resetTokenService.findByToken(token);

        if (!resetTokenService.checkExpirationIsAfterNow(resetToken)) {
            resetTokenService.updateUsed(resetToken, true);
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The provided token has expired").get();
        }

        return ResponseEntity.ok(Map.of(
                "message", "Token is valid"
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetUserPasswordRequest request) {
        String token = request.token();
        String email = request.email().toLowerCase();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password and confirmed password do not match").get();
        }

        Optional<Users> userLooked = userService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        if (resetTokenService.processNewResetPasswordTokensJustUsed(userLooked.get(), password, token)) {
            return okRequestCustomized("Contraseña restablecida exitosamente");
        } else {
            return badRequestCustomized("No se encontró ningún token válido para el usuario proporcionado");
        }
    }
}