package org.educa.homelyBackend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String audience,
        String issuer,
        String secretKey
) {
}
