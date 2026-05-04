package org.educa.homelyBackend.services.shared.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.services.shared.PasswordEncoderService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncoderServiceImpl implements PasswordEncoderService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean checkHashedPassword(String rawPassword, String hashPassword) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    @Override
    public String generateHashedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
