package org.educa.homelyBackend.routes;

public record ConfigurationRoutes() {

    public static final String OAUTH2 = "/oauth2";

    public static final String ADMIN = "/admin";

    public static final String API = "/api";

    public static final String DEFAULT = "/**";

    public static final String OAUTH2_ALL_ROUTES = OAUTH2 + DEFAULT;

    public static final String ADMIN_ALL_ROUTES = ADMIN + DEFAULT;

    public static final String API_ALL_ROUTES = API + DEFAULT;
}
