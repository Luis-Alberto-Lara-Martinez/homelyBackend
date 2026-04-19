package org.educa.homelyBackend.services.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
public class TokenService {

    private final String secretKey;
    private final String issuer;
    private final String audience;
    private final Clock clock;

    public TokenService(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience) {
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

    public String generatePersonalizedJwt(String email) {
        return generatePersonalizedJwt(email, null);
    }

    public String generatePersonalizedJwt(String email, Map<String, Object> extraClaims) {
        if (extraClaims == null) extraClaims = Map.of();
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
}