package org.educa.homelyBackend.service.domain;

import com.resend.core.exception.ResendException;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.entity.ResetPasswordTokens;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.repository.ResetPasswordTokensRepository;
import org.educa.homelyBackend.service.common.EmailService;
import org.educa.homelyBackend.service.common.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ResetPasswordTokensService {

    private static final Integer EXPIRATION_MINUTES = 20;

    private final ResetPasswordTokensRepository resetPasswordTokensRepository;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final UsersService usersService;

    public ResetPasswordTokensService(ResetPasswordTokensRepository resetPasswordTokensRepository, TokenService tokenService, EmailService emailService, UsersService usersService) {
        this.resetPasswordTokensRepository = resetPasswordTokensRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.usersService = usersService;
    }


    @Transactional(rollbackFor = Exception.class)
    public void processNewResetPasswordTokens(@NotNull Users user) throws ResendException {
        String resetToken = resetTokenService.generateSecureRandomToken();
        String hashResetToken = resetTokenService.hashRandomToken(resetToken);
        String resetLink = +"/reset-password?token=" + resetToken + "&email=" + user.getEmail();
        String expirationTime = EXPIRATION_MINUTES + " minutos";

        ResetPasswordTokens resetPasswordToken = new ResetPasswordTokens();
        resetPasswordToken.setIdUser(user);
        resetPasswordToken.setHashToken(resetTokenService.hashRandomToken(resetToken));
        resetPasswordToken.setExpiration(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTES)));

        saveResetPasswordTokens(resetPasswordToken);
        emailService.sendResetPasswordEmail(
                user.getEmail(),
                user.getName(),
                resetLink,
                expirationTime
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean processNewResetPasswordTokensJustUsed(Users user, String password, String token) {
        List<ResetPasswordTokens> resetPasswordTokensList = findByUserOrderByCreatedAtDesc(user);

        if (resetPasswordTokensList.isEmpty())
            return false;

        for (ResetPasswordTokens resetPasswordToken : resetPasswordTokensList) {
            if (passwordEncoderService.checkHash(token, resetPasswordToken.getHashToken())) {
                resetPasswordToken.setUsed(true);
                saveResetPasswordTokens(resetPasswordToken);

                user.setHashPassword(passwordEncoder.encode(password));
                usersService.saveUser(user);
                return true;
            }
        }

        return false;
    }

    public Optional<ResetPasswordTokens> findFirstByUserOrderByCreatedAtDesc(Users user) {
        return resetPasswordTokensRepository.findFirstByIdUserOrderByCreatedAtDesc(user);
    }

    public List<ResetPasswordTokens> findByUserOrderByCreatedAtDesc(Users user) {
        return resetPasswordTokensRepository.findByIdUserOrderByCreatedAtDesc(user);
    }

    public void saveResetPasswordTokens(ResetPasswordTokens resetPasswordToken) {
        resetPasswordTokensRepository.save(resetPasswordToken);
    }

    private void createNewResetPasswordToken(Users user, String hashToken) {
        ResetPasswordTokens resetPasswordToken = new ResetPasswordTokens();
        resetPasswordToken.setIdUser(user);
        resetPasswordToken.setHashToken(hashToken);
        resetPasswordToken.setExpiration(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTES)));

        resetPasswordTokensRepository.save(resetPasswordToken);
    }
}