package com.fleetops.notification.kafka.consumer;

import com.fleetops.notification.kafka.event.NotificationRequestEvent;
import com.fleetops.notification.notification.service.NotificationDispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequestConsumer {

    private final NotificationDispatchService dispatchService;

    @KafkaListener(
            topics = "${kafka.topics.notification-request}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onEvent(NotificationRequestEvent event) {
        log.info("Received NotificationRequestEvent | type={} | to={}",
                event.getType(), event.getRecipientEmail());
        dispatchService.dispatch(event);
    }
}
