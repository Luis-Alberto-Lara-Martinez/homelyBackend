package org.educa.homelyBackend.services.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class EncoderService {

    private final PasswordEncoder passwordEncoder;
    private final String secretKey;
    private final String issuer;
    private final String audience;

    public EncoderService(
            PasswordEncoder passwordEncoder, @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.issuer}") String issuer, @Value("${jwt.audience}") String audience
    ) {
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.audience = audience;
    }

    public String generateSecureRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[48];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public String generatePersonalizedJwt(String email, Map<String, Object> extraClaims) {
        Instant now = Instant.now();

        return Jwts.builder()
                .issuer(issuer)
                .audience().add(audience).and()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .claims(extraClaims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofDays(30))))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public boolean checkHashedPassword(String rawPassword, String hashPassword) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    public String generateHashedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public String generateHashedToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
