package com.brewbuddy.api;

import com.brewbuddy.api.dto.ChatRequest;
import com.brewbuddy.api.dto.ChatResponse;
import com.brewbuddy.app.AIAssistantService;
import com.brewbuddy.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIAssistantController {

    private final AIAssistantService aiAssistantService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UUID userId = userPrincipal.getUserId();
        String aiResponse = aiAssistantService.chat(
                request.getMessage(),
                userId,
                request.getIncludeUserBeverages()
        );

        ChatResponse response = ChatResponse.builder()
                .message(aiResponse)
                .role("assistant")
                .timestamp(OffsetDateTime.now())
                .contextIncluded(request.getIncludeUserBeverages())
                .build();

        return ResponseEntity.ok(response);
    }
}
