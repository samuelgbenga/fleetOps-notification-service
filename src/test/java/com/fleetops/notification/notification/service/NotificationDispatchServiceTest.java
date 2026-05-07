package com.fleetops.notification.notification.service;

import com.fleetops.notification.kafka.event.NotificationRequestEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class NotificationDispatchServiceTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationDispatchService dispatchService;

    @Test
    void dispatch_delegatesFullEventToEmailService() {
        NotificationRequestEvent event = NotificationRequestEvent.builder()
                .recipientEmail("driver@fleetops.com")
                .subject("Vehicle Alert")
                .message("Speed limit exceeded.")
                .type("VEHICLE_ALERT")
                .build();

        dispatchService.dispatch(event);

        verify(emailService).sendEmail(event);
    }

    @Test
    void dispatch_callsNoOtherChannels() {
        NotificationRequestEvent event = NotificationRequestEvent.builder()
                .recipientEmail("driver@fleetops.com")
                .subject("Alert")
                .message("Body")
                .type("GEOFENCE_BREACH")
                .build();

        dispatchService.dispatch(event);

        // Only email is wired — no SMS, no push
        verify(emailService).sendEmail(event);
        verifyNoMoreInteractions(emailService);
    }
}