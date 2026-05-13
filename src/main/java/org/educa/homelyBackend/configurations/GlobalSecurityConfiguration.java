package org.educa.homelyBackend.configurations;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.filters.JwtFilter;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.educa.homelyBackend.routes.Oauth2Routes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class GlobalSecurityConfiguration {

    private final JwtFilter jwtFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain oauth2Chain(HttpSecurity http) {
        return generateCommonSettings(http)
                .securityMatcher(ConfigurationRoutes.OAUTH2_ALL_ROUTES)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .authenticationManagerResolver(JwtIssuerAuthenticationManagerResolver.fromTrustedIssuers(
                                Oauth2Routes.GOOGLE,
                                Oauth2Routes.MICROSOFT
                        ))
                )
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiChain(HttpSecurity http) {
        return generateCommonSettings(http)
                .securityMatcher(ConfigurationRoutes.ADMIN_ALL_ROUTES, ConfigurationRoutes.API_ALL_ROUTES)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ConfigurationRoutes.ADMIN_ALL_ROUTES).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain defaultChain(HttpSecurity http) {
        return generateCommonSettings(http)
                .securityMatcher(ConfigurationRoutes.DEFAULT)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }

    private HttpSecurity generateCommonSettings(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
    }
}
