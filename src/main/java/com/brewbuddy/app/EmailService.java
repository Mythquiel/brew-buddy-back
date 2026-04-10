package com.brewbuddy.app;

import com.brewbuddy.api.dto.SupportFormDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${support.email}")
    private String supportEmail;

    public void sendSupportEmail(SupportFormDto form) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(supportEmail);
            message.setReplyTo(form.getEmail());
            message.setSubject("[Brew Buddy Support] " + form.getSubject());
            message.setText(buildEmailBody(form));

            mailSender.send(message);
            log.info("Support email sent successfully from: {}", form.getEmail());
        } catch (Exception e) {
            log.error("Failed to send support email", e);
            throw new RuntimeException("Failed to send support email: " + e.getMessage());
        }
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
