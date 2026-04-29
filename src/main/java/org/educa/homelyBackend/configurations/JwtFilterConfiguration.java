package org.educa.homelyBackend.configurations;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.filters.JwtFilter;
import org.educa.homelyBackend.properties.JwtProperties;
import org.educa.homelyBackend.utils.SigningKeyUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtFilterConfiguration {

    private final JwtProperties jwtProperties;
    private final SigningKeyUtil signingKeyUtil;

    @Bean
    public JwtParser jwtParser() {
        return Jwts.parser()
                .requireIssuer(jwtProperties.issuer())
                .requireAudience(jwtProperties.audience())
                .verifyWith(signingKeyUtil.getSigningKey())
                .build();
    }

    @Bean
    public JwtFilter jwtFilter(JwtParser jwtParser) {
        return new JwtFilter(jwtParser);
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
