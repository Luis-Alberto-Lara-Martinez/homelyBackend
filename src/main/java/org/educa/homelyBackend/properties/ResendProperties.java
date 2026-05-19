package org.educa.homelyBackend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resend")
public record ResendProperties(
        String apiKey
) {

    public static final String SECONDARY_EMAIL = "Homely Automático <no-reply@homelyweb.app>";

    public static final String PRIMARY_EMAIL = "Homely <comunications@homelyweb.app>";

    public static final String BASE_FRONTEND_URL = "https://homelyweb.app";
}
