package org.educa.homelyBackend.services.common;

import org.springframework.stereotype.Service;

@Service
public class IaService {

    // TODO: servicio sin hacer

//    private final WebClient webClient;
//    private final String model;
//
//    private final ChatClient chatClient;
//
//    public IaService(
//            @Value("${spring.ai.openai.api-key}") String apiKey,
//            @Value("${spring.ai.openai.base-url}") String baseUrl,
//            @Value("${spring.ai.openai.chat.options.model}") String model, ChatClient chatClient
//    ) {
//        this.model = model;
//        this.chatClient = chatClient;
//        this.webClient = WebClient.builder()
//                .baseUrl(baseUrl)
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
//
//    public String generarRespuesta(String mensaje) {
//        return chatClient.prompt(mensaje)
//                .call()
//                .content();
//    }
//
//    public String chat(
//            @NotBlank(message = "The prompt cannot be null nor empty")
//            String prompt
//    ) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", model);
//        requestBody.put("messages", new Object[]{
//                Map.of("role", "user", "content", prompt)
//        });
//
//        String responseString = webClient.post()
//                .uri("/v1/chat/completions")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        JsonNode root = mapper.readTree(responseString);
//        JsonNode contentNode = root
//                .path("choices")
//                .path(0)
//                .path("message")
//                .path("content");
//
//        return contentNode.isString() ? contentNode.stringValue() : "";
//    }
}
