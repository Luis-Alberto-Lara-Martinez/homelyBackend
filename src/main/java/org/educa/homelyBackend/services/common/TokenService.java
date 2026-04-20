package org.educa.homelyBackend.services.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@Validated
public class TokenService {

    private final PasswordEncoder passwordEncoder;
    private final String secretKey;
    private final String issuer;
    private final String audience;
    private final Clock clock;

    public TokenService(
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience
    ) {
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.audience = audience;
        this.clock = Clock.systemUTC();
    }

    public String generateSecureRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[48];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public String generatePersonalizedJwt(
            @NotBlank(message = "Email cannot be null or empty")
            @Email(message = "Invalid email format")
            String email
    ) {
        return generatePersonalizedJwt(email, null);
    }

    public String generatePersonalizedJwt(
            @NotBlank(message = "Email cannot be null or empty")
            @Email(message = "Invalid email format")
            String email,

            Map<String, Object> extraClaims
    ) {
        if (extraClaims == null) {
            extraClaims = Map.of();
        }

        Instant now = clock.instant();

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

    public boolean checkHashPassword(
            @NotBlank(message = "Raw password is null or empty")
            String rawPassword,

            @NotBlank(message = "Hash password is null or empty")
            String hashPassword
    ) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    public String generateHashPassword(
            @NotBlank(message = "Raw password is null or empty")
            String rawPassword
    ) {
        return passwordEncoder.encode(rawPassword);
    }

    public String generateHashToken(
            @NotBlank(message = "Token is null or empty")
            String token
    ) {
        return DigestUtils.sha256Hex(token);
    }
}