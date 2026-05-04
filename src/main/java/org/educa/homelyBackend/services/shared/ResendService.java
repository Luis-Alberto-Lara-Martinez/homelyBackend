package org.educa.homelyBackend.services.shared;

public interface ResendService {
    void sendWelcomeEmail(String to, String name);

    void sendResetPasswordEmail(String to, String name, String resetToken, Integer expirationMinutes);
}
