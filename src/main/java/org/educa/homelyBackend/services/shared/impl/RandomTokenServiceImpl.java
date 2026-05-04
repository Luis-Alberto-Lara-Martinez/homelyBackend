package org.educa.homelyBackend.services.shared.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.educa.homelyBackend.services.shared.RandomTokenService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class RandomTokenServiceImpl implements RandomTokenService {

    @Override
    public String generateRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[48];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    @Override
    public String generateHashedRandomToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
