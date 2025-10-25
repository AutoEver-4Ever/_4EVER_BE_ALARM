package org.ever._4ever_be_alarm.notification.adapter.kafka.in;

import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.notification.domain.port.in.NotificationSendUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationKafkaListener {

    private final NotificationSendUseCase notificationSendUseCase;


}
