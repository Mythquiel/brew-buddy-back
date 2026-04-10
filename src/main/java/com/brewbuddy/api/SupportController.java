package com.brewbuddy.api;

import com.brewbuddy.api.dto.SupportFormDto;
import com.brewbuddy.app.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Map<String, String>> submitSupportForm(@Valid @RequestBody SupportFormDto form) {
        emailService.sendSupportEmail(form);
        return ResponseEntity.ok(Map.of("message", "Support request submitted successfully"));
    }
}
