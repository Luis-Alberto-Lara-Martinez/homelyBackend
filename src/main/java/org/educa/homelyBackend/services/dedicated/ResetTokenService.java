package org.educa.homelyBackend.services.dedicated;

import com.resend.core.exception.ResendException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.daos.ResetTokenDao;
import org.educa.homelyBackend.models.ResetTokenModel;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.common.EmailService;
import org.educa.homelyBackend.services.common.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@Validated
public class ResetTokenService {

    private static final Integer EXPIRATION_MINUTES = 20;

    private final ResetTokenDao resetTokenDao;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final UserService userService;

    public ResetTokenService(ResetTokenDao resetTokenDao, TokenService tokenService, EmailService emailService, UserService userService) {
        this.resetTokenDao = resetTokenDao;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.userService = userService;
    }


    public Optional<ResetTokenModel> findByHashedToken(
            @NotBlank(message = "User cannot be null nor empty")
            String token
    ) {
        return resetTokenDao.findByHashedToken(tokenService.generateHashToken(token)).orElseThrow();
    }

    public ResetTokenModel saveOrUpdate(ResetTokenModel resetTokenModel) {
        return resetTokenDao.save(resetTokenModel);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createAndSendResetEmail(
            @NotNull(message = "User cannot be null")
            UserModel user
    ) throws ResendException {
        String resetToken = tokenService.generateSecureRandomToken();

        ResetTokenModel resetTokenModel = new ResetTokenModel();
        resetTokenModel.setUser(user);
        resetTokenModel.setHashedToken(tokenService.generateHashToken(resetToken));
        resetTokenModel.setExpiration(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTES)));

        saveOrUpdate(resetTokenModel);

        emailService.sendResetPasswordEmail(
                user.getEmail(),
                user.getName(),
                EXPIRATION_MINUTES,
                resetToken
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUsed(
            @NotBlank(message = "Token cannot be null nor empty")
            String token
    ) {
        Optional<ResetTokenModel> optionalResetTokenModel = findByHashedToken(token);

        if (optionalResetTokenModel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No reset token found for the provided token");
        }


    }
}
