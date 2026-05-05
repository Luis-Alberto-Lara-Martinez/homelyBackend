package org.educa.homelyBackend.facades.api.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.CheckResetTokenRequest;
import org.educa.homelyBackend.dtos.requests.ForgottenPasswordRequest;
import org.educa.homelyBackend.dtos.requests.ResetPasswordRequest;
import org.educa.homelyBackend.facades.api.ResetTokenApiFacade;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.properties.RandomTokenProperties;
import org.educa.homelyBackend.services.business.ResetTokenService;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.shared.RandomTokenService;
import org.educa.homelyBackend.services.shared.ResendService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ResetTokenApiFacadeImpl implements ResetTokenApiFacade {

    private final Clock clock;
    private final ResetTokenService resetTokenService;
    private final RandomTokenService randomTokenService;
    private final UserService userService;
    private final ResendService resendService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<Map<String, String>> forgottenPassword(ForgottenPasswordRequest request) {
        String token = randomTokenService.generateRandomToken();

        ResetTokenModel resetToken = resetTokenService.createResetToken(
                userService.findByEmailOrThrow(request.email().toLowerCase()),
                token
        );

        resendService.sendResetPasswordEmail(
                resetToken.getUser().getEmail(),
                resetToken.getUser().getName(),
                token,
                RandomTokenProperties.EXPIRATION_MINUTES
        );

        return ResponseEntityUtil.ok("Email de restablecimiento enviado correctamente");
    }

    @Override
    public ResponseEntity<Map<String, String>> checkResetToken(CheckResetTokenRequest request) {
        ResetTokenModel resetToken = resetTokenService.findByTokenOrThrow(request.token());

        if (resetToken.getExpiration().isBefore(Instant.now(clock)) || resetToken.getUsed()) {
            resetToken.setUsed(false);
            resetTokenService.save(resetToken);
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El token proporcionado está vencido o ya ha sido utilizado"
            ).get();
        }

        return ResponseEntityUtil.ok("Token válido");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<Map<String, String>> resetPassword(ResetPasswordRequest request) {
        String token = request.token();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden"
            ).get();
        }

        ResetTokenModel resetTokenModel = resetTokenService.updateUsed(resetTokenService.findByTokenOrThrow(token), true);

        userService.updateHashedPassword(resetTokenModel.getUser(), password);

        return ResponseEntityUtil.ok("Contraseña restablecida correctamente");
    }
}
