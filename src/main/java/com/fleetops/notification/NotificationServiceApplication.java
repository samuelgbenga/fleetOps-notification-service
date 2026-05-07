package com.fleetops.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Notification Service — stateless email dispatcher.
 *
 * Consumes NotificationRequestEvent from Kafka topic: notification.request
 * Sends emails via Mailtrap SMTP (JavaMailSender).
 * * No database. No JPA. No persistence layer.
 */
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
