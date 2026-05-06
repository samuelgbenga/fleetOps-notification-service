package com.fleetops.notification.kafka.consumer;

import com.fleetops.notification.kafka.event.NotificationRequestEvent;
import com.fleetops.notification.notification.service.NotificationDispatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationRequestConsumerTest {

    @Mock
    private NotificationDispatchService dispatchService;

    @InjectMocks
    private NotificationRequestConsumer consumer;

    @Test
    void onEvent_delegatesEventToDispatchService() {
        NotificationRequestEvent event = NotificationRequestEvent.builder()
                .recipientEmail("driver@fleetops.com")
                .recipientName("John Driver")
                .subject("Maintenance Due")
                .message("Vehicle 42 requires service.")
                .type("MAINTENANCE_DUE")
                .occurredAt(LocalDateTime.now())
                .build();

        consumer.onEvent(event);

        verify(dispatchService).dispatch(event);
    }

    @Test
    void onEvent_passesExactEventReferenceToDispatchService() {
        NotificationRequestEvent event = NotificationRequestEvent.builder()
                .recipientEmail("manager@fleetops.com")
                .subject("Geofence Breach")
                .message("Vehicle left zone.")
                .type("GEOFENCE_BREACH")
                .build();

        consumer.onEvent(event);

        verify(dispatchService).dispatch(event);
    }
}