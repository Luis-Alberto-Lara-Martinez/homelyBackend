package org.educa.homelyBackend.services.shared.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.properties.JwtProperties;
import org.educa.homelyBackend.services.shared.JwtService;
import org.educa.homelyBackend.utils.SigningKeyUtil;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final SigningKeyUtil signingKeyUtil;
    private final Clock clock;

    @Override
    public String generatePersonalizedJwt(String email, String role) {
        Instant now = Instant.now(clock);

        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .audience().add(jwtProperties.audience()).and()
                .subject(email)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofDays(30))))
                .signWith(signingKeyUtil.getSigningKey())
                .compact();
    }
}
