package org.educa.homelyBackend.routes;

public record ConfigurationRoutes() {

    public static String OAUTH2 = "/oauth2/**";

    public static String ADMIN = "/admin/**";

    public static String API = "/api/**";

    public static String DEFAULT = "/**";
}
