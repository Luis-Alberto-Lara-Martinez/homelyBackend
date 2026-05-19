package org.educa.homelyBackend.services.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.ResetTokenDao;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.services.business.ResetTokenService;
import org.educa.homelyBackend.services.shared.RandomTokenService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ResetTokenServiceImpl implements ResetTokenService {

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
    public ResetTokenModel save(ResetTokenModel resetToken) {
        return resetTokenDao.save(resetToken);
    }

    @Override
    public ResetTokenModel update(String token, ResetTokenModel resetTokenModel) {
        ResetTokenModel existingResetToken = findByTokenOrThrow(token);

        if (resetTokenModel.getUsed() != null) {
            existingResetToken.setUsed(resetTokenModel.getUsed());
            return save(existingResetToken);
        }

        return existingResetToken;
    }
}
