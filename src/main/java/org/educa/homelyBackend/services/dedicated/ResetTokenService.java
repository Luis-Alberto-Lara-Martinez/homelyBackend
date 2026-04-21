package org.educa.homelyBackend.services.dedicated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.daos.ResetTokenDao;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.common.EmailService;
import org.educa.homelyBackend.services.common.EncoderService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.Instant;

@Service
@Validated
public class ResetTokenService {

    private static final Integer EXPIRATION_MINUTES = 20;

    private final ResetTokenDao resetTokenDao;
    private final EncoderService encoderService;
    private final EmailService emailService;

    public ResetTokenService(ResetTokenDao resetTokenDao, EncoderService encoderService, EmailService emailService) {
        this.resetTokenDao = resetTokenDao;
        this.encoderService = encoderService;
        this.emailService = emailService;
    }

    public ResetTokenModel findByHashedToken(
            @NotBlank(message = "User cannot be null nor empty")
            String token
    ) {
        return resetTokenDao
                .findByHashedToken(encoderService.generateHashToken(token))
                .orElseThrow(ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No reset token found for the provided token"));
    }

    public ResetTokenModel saveOrUpdate(ResetTokenModel resetTokenModel) {
        return resetTokenDao.save(resetTokenModel);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createAndSendResetEmail(
            @NotNull(message = "User cannot be null")
            UserModel user
    ) {
        String resetToken = encoderService.generateSecureRandomToken();

        ResetTokenModel resetTokenModel = new ResetTokenModel();
        resetTokenModel.setUser(user);
        resetTokenModel.setHashedToken(encoderService.generateHashToken(resetToken));
        resetTokenModel.setExpiration(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTES)));

        resetTokenModel = saveOrUpdate(resetTokenModel);

        emailService.sendResetPasswordEmail(
                resetTokenModel.getUser().getEmail(),
                resetTokenModel.getUser().getName(),
                EXPIRATION_MINUTES,
                resetToken
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUsed(
            @NotBlank(message = "Token cannot be null nor empty")
            String token,

            @NotNull(message = "Used cannot be null")
            Boolean used
    ) {
        ResetTokenModel resetToken = findByHashedToken(token);
        resetToken.setUsed(used);

        saveOrUpdate(resetToken);
    }
}
