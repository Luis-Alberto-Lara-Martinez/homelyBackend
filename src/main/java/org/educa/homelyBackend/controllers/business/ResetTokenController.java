package org.educa.homelyBackend.controllers.business;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.CheckResetTokenStateDtoRequest;
import org.educa.homelyBackend.dtos.requests.ForgottenPasswordDtoRequest;
import org.educa.homelyBackend.dtos.requests.ResetPasswordDtoRequest;
import org.educa.homelyBackend.facades.business.ResetTokenFacade;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ResetTokenController {

    private final ResetTokenFacade resetTokenFacade;

    @PostMapping("/forgotten-password")
    public ResponseEntity<Map<String, String>> forgottenPassword(@Valid @RequestBody ForgottenPasswordDtoRequest request) {
        return resetTokenFacade.forgottenPassword(request);
    }

    @PostMapping("/check-reset-token")
    public ResponseEntity<Map<String, String>> checkResetToken(@Valid @RequestBody CheckResetTokenStateDtoRequest request) {
        resetTokenFacade.checkResetToken(request);
        return ResponseEntityUtil.ok("Token válido");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDtoRequest request) {
        resetTokenFacade.resetPassword(request);
        return ResponseEntityUtil.ok("Contraseña restablecida correctamente");
    }
}