package org.educa.homelyBackend.services.shared.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.properties.GroqProperties;
import org.educa.homelyBackend.services.shared.GroqService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroqServiceImpl implements GroqService {

    private final GroqProperties groqProperties;
    private final WebClient webClient;

    @Override
    public String generatePropertyDescription(String instructions) {
        String prompt = GroqProperties.BASE_PROPMT.formatted(instructions);
        JsonNode response = callGroqApi(prompt);
        return extractDescriptionFromResponse(response);
    }

    private JsonNode callGroqApi(String prompt) {
        JsonNode response = webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(createRequestPayload(prompt))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null || !response.has("choices")) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Groq API error: respuesta inválida"
            ).get();
        }

        return response;
    }

    private Map<String, Object> createRequestPayload(String prompt) {
        return Map.of(
                "model", groqProperties.model(),
                "messages", List.of(Map.of(
                        "role", "user",
                        "content", prompt
                ))
        );
    }

    private String extractDescriptionFromResponse(JsonNode response) {
        JsonNode contentNode = response.path("choices")
                .get(0)
                .path("message")
                .path("content");

        if (contentNode.isMissingNode()) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Groq API error: no content en respuesta"
            ).get();
        }

        String content = contentNode.asString().trim();

        if (content.isEmpty()) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Groq API error: contenido vacío"
            ).get();
        }

        return content;
    }
}

