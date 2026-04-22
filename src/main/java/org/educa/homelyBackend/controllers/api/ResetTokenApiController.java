package org.educa.homelyBackend.controllers.api;

import jakarta.validation.Valid;
import org.educa.homelyBackend.dtos.request.CheckResetTokenRequest;
import org.educa.homelyBackend.dtos.request.ForgetUserPasswordRequest;
import org.educa.homelyBackend.dtos.request.ResetUserPasswordRequest;
import org.educa.homelyBackend.services.dedicated.ResetTokenService;
import org.educa.homelyBackend.services.dedicated.UserService;
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
public class ResetTokenApiController {

    private final ResetTokenService resetTokenService;
    private final UserService userService;

    public ResetTokenApiController(ResetTokenService resetTokenService, UserService userService) {
        this.resetTokenService = resetTokenService;
        this.userService = userService;
    }

    @PostMapping("/forgotten-password")
    public ResponseEntity<Map<String, String>> forgottenPassword(@Valid @RequestBody ForgetUserPasswordRequest request) {
        String email = request.email().toLowerCase();

        resetTokenService.createAndSendResetEmail(userService.findByEmailOrThrow(email));

        return ResponseEntityUtil.ok("An email has been sent to reset the password");
    }

    @GetMapping("/check-reset-token")
    public ResponseEntity<Map<String, String>> checkResetToken(@Valid @RequestBody CheckResetTokenRequest request) {
        String token = request.token();

        resetTokenService.checkTokenAndUpdateIfExpired(resetTokenService.findByTokenOrThrow(token));

        return ResponseEntityUtil.ok("Token is valid");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetUserPasswordRequest request) {
        String token = request.token();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password and confirmed password do not match").get();
        }

        resetTokenService.updateUserAndUsed(resetTokenService.findByTokenOrThrow(token), password);

        return ResponseEntityUtil.ok("Password successfully reset");
    }
}