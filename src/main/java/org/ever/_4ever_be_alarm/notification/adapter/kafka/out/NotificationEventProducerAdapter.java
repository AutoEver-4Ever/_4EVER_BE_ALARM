package org.ever._4ever_be_alarm.notification.adapter.kafka.out;

import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.Notification;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;
import org.springframework.stereotype.Component;

@Component("${dispatch.strategy-names.sse}")
public class NotificationEventProducerAdapter implements NotificationDispatchPort {

    @Override
    public void dispatch(Notification alarm) {

    }
}
