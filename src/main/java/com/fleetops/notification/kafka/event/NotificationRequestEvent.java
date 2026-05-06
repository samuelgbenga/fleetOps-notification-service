package com.fleetops.notification.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Shared Kafka event contract.
 * Must stay in sync with com.fleetops.core.kafka.event.NotificationRequestEvent
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestEvent {
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String message;
    private String type;
    private LocalDateTime occurredAt;
}
