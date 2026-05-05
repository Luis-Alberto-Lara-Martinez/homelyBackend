package org.educa.homelyBackend.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.CheckResetTokenRequest;
import org.educa.homelyBackend.dtos.requests.ForgottenPasswordRequest;
import org.educa.homelyBackend.dtos.requests.ResetPasswordRequest;
import org.educa.homelyBackend.facades.api.ResetTokenApiFacade;
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

    private final ResetTokenApiFacade resetTokenApiFacade;

    @PostMapping("/forgotten-password")
    public ResponseEntity<Map<String, String>> forgottenPassword(@Valid @RequestBody ForgottenPasswordRequest request) {
        return resetTokenApiFacade.forgottenPassword(request);
    }

    @GetMapping("/check-reset-token")
    public ResponseEntity<Map<String, String>> checkResetToken(@Valid @RequestBody CheckResetTokenRequest request) {
        return resetTokenApiFacade.checkResetToken(request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return resetTokenApiFacade.resetPassword(request);
    }
}