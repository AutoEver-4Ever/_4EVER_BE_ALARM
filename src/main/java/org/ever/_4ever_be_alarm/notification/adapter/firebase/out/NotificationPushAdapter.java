package org.ever._4ever_be_alarm.notification.adapter.firebase.out;

import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.Notification;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;
import org.springframework.stereotype.Component;

@Component("${dispatch.strategy-names.app-push}")
public class NotificationPushAdapter implements NotificationDispatchPort {

    @Override
    public void dispatch(Notification alarm) {

    }
}
