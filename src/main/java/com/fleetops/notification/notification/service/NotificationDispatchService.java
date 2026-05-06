package com.fleetops.notification.notification.service;

import com.fleetops.notification.kafka.event.NotificationRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationDispatchService {

    private final EmailService emailService;

    /**
     * Entry point for all incoming notification events.
     * Routes to the appropriate channel based on type or defaults to email.
     *
     * Current channels: EMAIL (active)
     * Future channels : SMS (stub), PUSH (stub)
     */
    public void dispatch(NotificationRequestEvent event) {
        log.info("Dispatching notification | type={} | recipient={}",
                event.getType(), event.getRecipientEmail());

        // Primary channel — email via Mailtrap
        emailService.sendEmail(event);

        // TODO: wire SmsService when SMS provider is configured
        // TODO: wire PushService when push tokens are available
    }
}
