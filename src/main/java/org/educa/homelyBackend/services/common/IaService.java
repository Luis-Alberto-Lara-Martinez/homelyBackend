package org.educa.homelyBackend.services.common;

import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Service
public class IaService {

    // TODO: Revisar el servicio

    private final WebClient webClient;
    private final String model;

    public IaService(
            @Value("${groq.api.key}") String apiKey,
            @Value("${groq.base.url}") String baseUrl,
            @Value("${groq.model}") String model
    ) {
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String chat(String message) {
        JsonNode root = webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(Map.of(
                        "model", model,
                        "messages", List.of(Map.of(
                                "role", "user",
                                "content", message
                        ))
                ))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (root == null || !root.has("choices")) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "Fallo en la petición a Groq").get();
        }

        return root.path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asString();
    }
}
