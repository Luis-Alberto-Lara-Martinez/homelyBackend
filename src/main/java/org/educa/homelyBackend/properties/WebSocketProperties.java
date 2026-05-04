package org.educa.homelyBackend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "websocket")
public record WebSocketProperties(
        String appPrefix,
        String endpoint,
        String topicPrefix
) {
    public static final String[] ALLOWED_ORIGINS = CorsProperties.ALLOWED_ORIGINS.toArray(String[]::new);
}
