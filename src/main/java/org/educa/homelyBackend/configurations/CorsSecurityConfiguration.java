package org.educa.homelyBackend.configurations;

import org.educa.homelyBackend.properties.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsSecurityConfiguration {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(CorsProperties.ALLOWED_ORIGINS);
        configuration.setAllowedMethods(CorsProperties.ALLOWED_METHODS);
        configuration.setAllowedHeaders(CorsProperties.ALLOWED_HEADERS);
        configuration.setAllowCredentials(CorsProperties.ALLOW_CREDENTIALS);
        configuration.setMaxAge(CorsProperties.MAX_AGE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
