package com.brewbuddy.app;

import com.brewbuddy.api.dto.SupportFormDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final WebClient.Builder webClientBuilder;

    @Value("${support.email}")
    private String supportEmail;

    @Value("${support.email.from}")
    private String supportEmailFrom;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.api.url}")
    private String resendApiUrl;

    public void sendSupportEmail(SupportFormDto form) {
        try {
            if (resendApiKey != null && !resendApiKey.isBlank()) {
                sendWithResend(form);
                return;
            }

            sendWithSmtp(form);
        } catch (Exception e) {
            log.error("Failed to send support email", e);
            throw new RuntimeException("Failed to send support email: " + e.getMessage());
        }
    }

    private void sendWithSmtp(SupportFormDto form) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(supportEmail);
            message.setReplyTo(form.getEmail());
            message.setSubject("[Brew Buddy Support] " + form.getSubject());
            message.setText(buildEmailBody(form));

            mailSender.send(message);
            log.info("Support email sent successfully from: {}", form.getEmail());
    }

    private void sendWithResend(SupportFormDto form) {
        Map<String, Object> requestBody = Map.of(
                "from", supportEmailFrom,
                "to", supportEmail,
                "reply_to", form.getEmail(),
                "subject", "[Brew Buddy Support] " + form.getSubject(),
                "text", buildEmailBody(form)
        );

        webClientBuilder.build()
                .post()
                .uri(resendApiUrl)
                .header("Authorization", "Bearer " + resendApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .block();

        log.info("Support email sent successfully through Resend from: {}", form.getEmail());
    }

    private String buildEmailBody(SupportFormDto form) {
        return String.format(
                """
                Support Request from Brew Buddy

                From: %s (%s)

                Subject: %s

                Message:
                %s

                ---
                This email was sent from the Brew Buddy support form.
                """,
                form.getName(),
                form.getEmail(),
                form.getSubject(),
                form.getMessage()
        );
    }
}
