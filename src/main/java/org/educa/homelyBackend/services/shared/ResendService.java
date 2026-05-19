package org.educa.homelyBackend.services.shared;

import org.springframework.web.multipart.MultipartFile;

public interface ResendService {

    void sendWelcomeEmail(String to, String name);

    void sendResetPasswordEmail(String to, String name, String resetToken, Integer expirationMinutes);

    void sendWorkWithUsEmail(String from, String name, String workingArea, String phone, String description, MultipartFile cvFile);

    void sendContactEmail(String name, String email, String message);
}
