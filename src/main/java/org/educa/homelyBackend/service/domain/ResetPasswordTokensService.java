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
import java.util.Optional;

@Service
public class ResetPasswordTokensService {

    private static final String BASE_FRONTEND_URL = "https://www.homelyweb.app";
    private static final Integer EXPIRATION_MINUTES = 20;

    private final ResetPasswordTokensRepository resetPasswordTokensRepository;
    private final ResetTokenService resetTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UsersService usersService;

    public ResetPasswordTokensService(ResetPasswordTokensRepository resetPasswordTokensRepository, ResetTokenService resetTokenService, EmailService emailService, PasswordEncoder passwordEncoder, UsersService usersService) {
        this.resetPasswordTokensRepository = resetPasswordTokensRepository;
        this.resetTokenService = resetTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
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

    @Transactional(rollbackFor = Exception.class)
    public boolean processNewResetPasswordTokensJustUsed(Users user, String password, String token) {
        List<ResetPasswordTokens> resetPasswordTokensList = findByUserOrderByCreatedAtDesc(user);

        if (resetPasswordTokensList.isEmpty())
            return false;

        for (ResetPasswordTokens resetPasswordToken : resetPasswordTokensList) {
            if (checkToken(token, resetPasswordToken.getHashToken())) {
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

    public boolean checkToken(String rawToken, String hashToken) {
        return passwordEncoder.matches(rawToken, hashToken);
    }
}