package org.educa.homelyBackend.services.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.ResetTokenDao;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.shared.impl.ResendServiceImpl;
import org.educa.homelyBackend.services.shared.impl.RandomTokenServiceImpl;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ResetTokenService {

    private static final Integer EXPIRATION_MINUTES = 20;

    private final ResetTokenDao resetTokenDao;
    private final RandomTokenServiceImpl randomTokenServiceImpl;
    private final ResendServiceImpl resendServiceImpl;
    private final UserService userService;

    public ResetTokenModel findByTokenOrThrow(String token) {
        return resetTokenDao.findByHashedToken(randomTokenServiceImpl.generateHashedRandomToken(token))
                .orElseThrow(() -> ExceptionUtil.manageException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró ningún token de restablecimiento de contraseña válido para el token proporcionado"
                ).get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void checkTokenAndUpdateIfExpired(ResetTokenModel resetToken) {
        if (resetToken.getExpiration().isBefore(Instant.now()) || resetToken.getUsed()) {
            resetToken.setUsed(false);
            saveOrUpdate(resetToken);
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El token proporcionado está vencido o ya ha sido utilizado"
            ).get();
        }
    }

    public ResetTokenModel saveOrUpdate(ResetTokenModel resetTokenModel) {
        return resetTokenDao.save(resetTokenModel);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createAndSendResetEmail(UserModel user) {
        String token = randomTokenServiceImpl.generateRandomToken();

        ResetTokenModel resetTokenModel = new ResetTokenModel();
        resetTokenModel.setUser(user);
        resetTokenModel.setHashedToken(randomTokenServiceImpl.generateHashedRandomToken(token));
        resetTokenModel.setExpiration(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTES)));

        resetTokenModel = saveOrUpdate(resetTokenModel);

        resendServiceImpl.sendResetPasswordEmail(
                resetTokenModel.getUser().getEmail(),
                resetTokenModel.getUser().getName(),
                token,
                EXPIRATION_MINUTES
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndUsed(ResetTokenModel resetToken, String password) {
        resetToken.setUsed(true);
        saveOrUpdate(resetToken);
        userService.updateHashedPassword(resetToken.getUser(), password);
    }
}
