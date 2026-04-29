package org.educa.homelyBackend.utils;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.properties.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class SigningKeyUtil {

    private final JwtProperties jwtProperties;

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
    }
}
