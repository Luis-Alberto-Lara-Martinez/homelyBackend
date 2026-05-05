package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.ResetTokenDao;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.ResetTokenService;
import org.educa.homelyBackend.services.shared.RandomTokenService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ResetTokenServiceImpl implements ResetTokenService {

    private static final Integer EXPIRATION_MINUTES = 20;

    private final ResetTokenDao resetTokenDao;
    private final RandomTokenService randomTokenService;
    private final Clock clock;

    @Override
    public ResetTokenModel findByTokenOrThrow(String token) {
        return resetTokenDao.findByHashedToken(randomTokenService.generateHashedRandomToken(token))
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró ningún token de restablecimiento válido"
                ).get());
    }

    @Override
    public ResetTokenModel createResetToken(UserModel user, String token) {
        return save(ResetTokenModel.builder()
                .user(user)
                .used(false)
                .hashedToken(randomTokenService.generateHashedRandomToken(token))
                .expiration(Instant.now(clock).plus(Duration.ofMinutes(EXPIRATION_MINUTES)))
                .build());
    }

    @Override
    public ResetTokenModel save(ResetTokenModel resetToken) {
        return resetTokenDao.save(resetToken);
    }

    @Override
    public ResetTokenModel updateUsed(ResetTokenModel resetToken, boolean used) {
        resetToken.setUsed(used);
        return save(resetToken);
    }
}
