package org.educa.homelyBackend.services.shared;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class RandomTokenService {

    public String generateSecureRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[48];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public String generateHashedToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
