package com.fleetops.notification.notification.service;

import com.fleetops.notification.kafka.event.NotificationRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${notification.from-email}")
    private String fromEmail;

    @Value("${notification.from-name}")
    private String fromName;

    public void sendEmail(NotificationRequestEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromName + " <" + fromEmail + ">");
            message.setTo(event.getRecipientEmail());
            message.setSubject(event.getSubject());
            message.setText(event.getMessage());

            mailSender.send(message);

            log.info("Email sent successfully | type={} | to={} | subject={}",
                    event.getType(),
                    event.getRecipientEmail(),
                    event.getSubject());

        } catch (Exception e) {
            log.error("Failed to send email | type={} | to={} | error={}",
                    event.getType(),
                    event.getRecipientEmail(),
                    e.getMessage());
        }
    }
}
