package com.fleetops.notification.notification.service;

import com.fleetops.notification.kafka.event.NotificationRequestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void injectValueFields() {
        // @Value fields are not in the constructor — set them via reflection
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@fleetops.com");
        ReflectionTestUtils.setField(emailService, "fromName", "FleetOps System");
    }

    @Test
    void sendEmail_buildsCorrectMessageAndCallsSend() {
        NotificationRequestEvent event = NotificationRequestEvent.builder()
                .recipientEmail("driver@fleetops.com")
                .subject("Geofence Alert")
                .message("Vehicle left the assigned zone.")
                .type("GEOFENCE_BREACH")
                .build();

        emailService.sendEmail(event);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertThat(sent.getFrom()).isEqualTo("noreply@fleetops.com");
        assertThat(sent.getTo()).containsExactly("driver@fleetops.com");
        assertThat(sent.getSubject()).isEqualTo("Geofence Alert");
        assertThat(sent.getText()).isEqualTo("Vehicle left the assigned zone.");
    }

    @Test
    void sendEmail_whenSmtpFails_doesNotPropagateException() {
        doThrow(new MailSendException("SMTP timeout"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        NotificationRequestEvent event = NotificationRequestEvent.builder()
                .recipientEmail("driver@fleetops.com")
                .subject("Alert")
                .message("Vehicle off route")
                .type("VEHICLE_ALERT")
                .build();
        assertThatCode(() -> emailService.sendEmail(event)).doesNotThrowAnyException();
    }

    @Test
    void sendEmail_setsFromAddressFromConfig() {
        NotificationRequestEvent event = NotificationRequestEvent.builder()
                .recipientEmail("manager@fleetops.com")
                .subject("Maintenance Due")
                .message("Vehicle 42 requires service.")
                .type("MAINTENANCE_DUE")
                .build();

        emailService.sendEmail(event);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        assertThat(captor.getValue().getFrom()).isEqualTo("noreply@fleetops.com");
    }
}