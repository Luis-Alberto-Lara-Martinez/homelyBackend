package org.educa.homelyBackend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloudinary")
public record CloudinaryProperties(
        String cloudName,
        String apiKey,
        String apiSecret
) {

    private static final String BASE_DIRECTORY = "homely";

    public static final String AVATARS_DIRECTORY = BASE_DIRECTORY + "/avatars";

    public static final String PROPERTIES_DIRECTORY = BASE_DIRECTORY + "/properties";
}
