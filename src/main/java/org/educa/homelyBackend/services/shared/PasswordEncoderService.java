package org.educa.homelyBackend.services.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncoderService {

    private final PasswordEncoder passwordEncoder;

    public boolean checkHashedPassword(String rawPassword, String hashPassword) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    public String generateHashedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
