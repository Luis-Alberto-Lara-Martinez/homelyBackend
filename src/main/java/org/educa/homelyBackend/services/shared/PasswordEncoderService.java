package org.educa.homelyBackend.services.shared;

public interface PasswordEncoderService {
    boolean checkHashedPassword(String rawPassword, String hashPassword);

    String generateHashedPassword(String rawPassword);
}
