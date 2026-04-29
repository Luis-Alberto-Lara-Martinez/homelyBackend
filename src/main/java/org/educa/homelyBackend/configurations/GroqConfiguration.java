package org.educa.homelyBackend.configurations;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.educa.homelyBackend.properties.GroqProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class GroqConfiguration {

    private final GroqProperties groqProperties;

    @Bean
    public WebClient groqWebClient() {
        return WebClient.builder()
                .baseUrl(groqProperties.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + groqProperties.apiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
