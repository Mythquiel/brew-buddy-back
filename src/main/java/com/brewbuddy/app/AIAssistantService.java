package com.brewbuddy.app;

import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BrewLogDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIAssistantService {

    private final WebClient.Builder webClientBuilder;
    private final BeverageService beverageService;
    private final BrewLogService brewLogService;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private static final String SYSTEM_PROMPT = """
            You are a friendly tea and coffee expert assistant for Brew Buddy, an app that helps users track and brew their favorite beverages.
            
            STRICT RULES:
            1. ONLY answer questions about tea, coffee, brewing methods, beverage recommendations, and related topics
            2. If asked about anything unrelated (politics, sports, other topics), politely respond:
               "I'm your tea & coffee expert! ☕🍵 I can help with brewing tips, beverage recommendations, or questions about your collection. What would you like to know?"
            3. Be concise and helpful - keep responses under 200 words unless detailed instructions are needed
            4. Use the provided user data to give personalized recommendations
            5. Be friendly and use emojis occasionally (☕🍵🫖) but don't overdo it
            
            When giving brewing advice:
            - Don't say hello - there is already hardcoded hello message
            - Provide specific water temperatures and steep times
            - Mention the type of tea/coffee if relevant
            - Give practical tips
            
            """;

    public String chat(String userMessage, UUID userId, boolean includeContext) {
        try {
            String context = includeContext ? buildUserContext(userId) : "";
            String fullPrompt = SYSTEM_PROMPT + context + "\n\nUser: " + userMessage + "\n\nAssistant:";

            log.debug("Sending message to Gemini API. User: {}, Context included: {}", userId, includeContext);

            return callGeminiApi(fullPrompt);

        } catch (Exception e) {
            log.error("Error calling AI service for user {}: {}", userId, e.getMessage(), e);
            return "I'm having trouble connecting right now. Please try again in a moment! ☕";
        }
    }

    private String buildUserContext(UUID userId) {
        try {
            StringBuilder context = new StringBuilder("\n\nUSER'S CONTEXT:\n");

            Pageable pageable = PageRequest.of(0, 10);
            Page<BeverageDto> beverages = beverageService.list(null, null, null, pageable);

            if (!beverages.isEmpty()) {
                context.append("\nUser's Beverage Collection:\n");
                beverages.getContent().forEach(bev -> {
                    context.append(String.format("- %s (%s)%s\n",
                            bev.getName(),
                            bev.getType(),
                            bev.getBrand() != null ? " by " + bev.getBrand() : ""
                    ));
                });
            }

            try {
                Page<BrewLogDto> recentLogs = brewLogService.list(
                        null,
                        OffsetDateTime.now().minusDays(7),
                        null,
                        userId,
                        PageRequest.of(0, 5)
                );

                if (!recentLogs.isEmpty()) {
                    context.append("\nRecent Brewing Activity (last 7 days):\n");
                    recentLogs.getContent().forEach(log -> {
                        context.append(String.format("- Brewed on %s\n",
                                log.getBrewedAt().toLocalDate()
                        ));
                    });
                }
            } catch (Exception e) {
                log.error("Couldn't get brew logs for user {}: {}", userId, e.getMessage(), e);
            }

            return context.toString();

        } catch (Exception e) {
            log.warn("Failed to build user context: {}", e.getMessage());
            return "";
        }
    }

    private String callGeminiApi(String prompt) {
        WebClient webClient = webClientBuilder.build();

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        log.debug("Calling Gemini API at: {}", geminiApiUrl);
        log.debug("Request body: {}", requestBody);

        try {
            String response = webClient.post()
                    .uri(geminiApiUrl)
                    .header("X-goog-api-key", geminiApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .doOnNext(errorBody -> log.error("Gemini API error response: {}", errorBody))
                                    .then(clientResponse.createException())
                    )
                    .bodyToMono(String.class)
                    .block();

            return parseGeminiResponse(response);

        } catch (Exception e) {
            log.error("Error calling Gemini API: URL={}, Error={}", geminiApiUrl, e.getMessage());
            throw new RuntimeException("Failed to get AI response", e);
        }
    }

    private String parseGeminiResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode candidates = root.path("candidates");

            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode content = candidates.get(0).path("content");
                JsonNode parts = content.path("parts");

                if (parts.isArray() && !parts.isEmpty()) {
                    return parts.get(0).path("text").asText();
                }
            }

            log.warn("Unexpected Gemini API response format");
            return "I couldn't process that properly. Could you rephrase your question?";

        } catch (Exception e) {
            log.error("Error parsing Gemini response: {}", e.getMessage());
            return "I encountered an error processing the response. Please try again!";
        }
    }
}
