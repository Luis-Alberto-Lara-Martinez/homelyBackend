package org.educa.homelyBackend.properties;

import java.util.List;

public record CorsProperties() {
    public static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:4200", "https://homelyweb.app", "https://www.homelyweb.app"
    );

    public static final List<String> ALLOWED_METHODS = List.of(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
    );

    public static final List<String> ALLOWED_HEADERS = List.of(
            "Authorization", "Content-Type", "Accept"
    );

    public static final boolean ALLOW_CREDENTIALS = true;

    public static final long MAX_AGE = 3600L;
}
