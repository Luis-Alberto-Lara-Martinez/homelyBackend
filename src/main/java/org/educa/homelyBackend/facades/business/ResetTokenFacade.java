package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.requests.CheckResetTokenStateDtoRequest;
import org.educa.homelyBackend.dtos.requests.ForgottenPasswordDtoRequest;
import org.educa.homelyBackend.dtos.requests.ResetPasswordDtoRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ResetTokenFacade {
    ResponseEntity<Map<String, String>> forgottenPassword(ForgottenPasswordDtoRequest request);

    void checkResetToken(CheckResetTokenStateDtoRequest request);

    void resetPassword(ResetPasswordDtoRequest request);
}
