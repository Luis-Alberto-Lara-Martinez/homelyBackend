package org.educa.homelyBackend.facades.api;

import org.educa.homelyBackend.dtos.requests.CheckResetTokenRequest;
import org.educa.homelyBackend.dtos.requests.ForgottenPasswordRequest;
import org.educa.homelyBackend.dtos.requests.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ResetTokenApiFacade {
    ResponseEntity<Map<String, String>> forgottenPassword(ForgottenPasswordRequest request);

    ResponseEntity<Map<String, String>> checkResetToken(CheckResetTokenRequest request);

    ResponseEntity<Map<String, String>> resetPassword(ResetPasswordRequest request);
}
