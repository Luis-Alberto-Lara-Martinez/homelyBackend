package org.educa.homelyBackend.service.domain;

import com.resend.core.exception.ResendException;
import org.educa.homelyBackend.entity.ResetPasswordTokens;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.repository.ResetPasswordTokensRepository;
import org.educa.homelyBackend.service.common.EmailService;
import org.educa.homelyBackend.service.common.ResetTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class ResetPasswordTokensService {

    private static final String BASE_FRONTEND_URL = "https://www.homelyweb.app";
    private static final Integer EXPIRATION_MINUTES = 20;

    private final ResetPasswordTokensRepository resetPasswordTokensRepository;
    private final ResetTokenService resetTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordTokensService(ResetPasswordTokensRepository resetPasswordTokensRepository, ResetTokenService resetTokenService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.resetPasswordTokensRepository = resetPasswordTokensRepository;
        this.resetTokenService = resetTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackFor = Exception.class)
    public void processNewResetPasswordTokens(Users user) throws ResendException {
        String resetToken = resetTokenService.generateSecureRandomToken();
        String hashResetToken = resetTokenService.hashRandomToken(resetToken);
        String resetLink = BASE_FRONTEND_URL + "/reset-password?token=" + resetToken + "&email=" + user.getEmail();
        String expirationTime = EXPIRATION_MINUTES + " minutos";

        ResetPasswordTokens resetPasswordToken = new ResetPasswordTokens();
        resetPasswordToken.setIdUser(user);
        resetPasswordToken.setHashToken(hashResetToken);
        resetPasswordToken.setExpiration(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTES)));

        saveResetPasswordTokens(resetPasswordToken);
        emailService.sendResetPasswordEmail(
                user.getEmail(),
                user.getName(),
                resetLink,
                expirationTime
        );
    }

    public List<ResetPasswordTokens> findByUser(Users user) {
        return resetPasswordTokensRepository.findByIdUser(user);
    }

    public void saveResetPasswordTokens(ResetPasswordTokens resetPasswordToken) {
        resetPasswordTokensRepository.save(resetPasswordToken);
    }

    public boolean checkToken(String rawToken, String hashToken) {
        return passwordEncoder.matches(rawToken, hashToken);
    }
}