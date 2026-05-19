package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.CheckResetTokenStateDtoRequest;
import org.educa.homelyBackend.dtos.requests.ForgottenPasswordDtoRequest;
import org.educa.homelyBackend.dtos.requests.ResetPasswordDtoRequest;
import org.educa.homelyBackend.facades.business.ResetTokenFacade;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.properties.RandomTokenProperties;
import org.educa.homelyBackend.services.business.ResetTokenService;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.shared.PasswordEncoderService;
import org.educa.homelyBackend.services.shared.RandomTokenService;
import org.educa.homelyBackend.services.shared.ResendService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ResetTokenFacadeImpl implements ResetTokenFacade {

    private final Clock clock;
    private final ResetTokenService resetTokenService;
    private final RandomTokenService randomTokenService;
    private final UserService userService;
    private final ResendService resendService;
    private final PasswordEncoderService passwordEncoderService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<Map<String, String>> forgottenPassword(ForgottenPasswordDtoRequest request) {
        String token = randomTokenService.generateRandomToken();

        ResetTokenModel resetTokenModel = resetTokenService.save(
                ResetTokenModel.builder()
                        .user(userService.findByEmailOrThrow(request.email().toLowerCase()))
                        .hashedToken(randomTokenService.generateHashedRandomToken(token))
                        .expiration(Instant.now(clock).plus(Duration.ofMinutes(RandomTokenProperties.EXPIRATION_MINUTES)))
                        .used(false)
                        .build()
        );

        resendService.sendResetPasswordEmail(
                resetTokenModel.getUser().getEmail(),
                resetTokenModel.getUser().getName(),
                token,
                RandomTokenProperties.EXPIRATION_MINUTES
        );

        return ResponseEntityUtil.ok("Email de restablecimiento enviado correctamente");
    }

    @Override
    public void checkResetToken(CheckResetTokenStateDtoRequest request) {
        ResetTokenModel resetToken = resetTokenService.findByTokenOrThrow(request.token());

        if (resetToken.getExpiration().isBefore(Instant.now(clock)) || resetToken.getUsed()) {
            resetToken.setUsed(true);
            resetTokenService.save(resetToken);
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El token proporcionado está vencido o ya ha sido utilizado"
            ).get();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetPassword(ResetPasswordDtoRequest request) {
        String token = request.token();
        String password = request.password();

        if (!password.equals(request.confirmedPassword())) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden"
            ).get();
        }

        ResetTokenModel resetTokenModel = resetTokenService.update(
                token,
                ResetTokenModel.builder()
                        .used(true)
                        .build()
        );

        userService.update(
                resetTokenModel.getUser(),
                UserModel.builder()
                        .hashedPassword(passwordEncoderService.generateHashedPassword(password))
                        .build()
        );
    }
}
