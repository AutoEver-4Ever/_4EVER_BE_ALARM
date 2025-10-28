package org.ever._4ever_be_alarm.notification.adapter.firebase.out;

import org.ever._4ever_be_alarm.notification.domain.model.Noti;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;
import org.springframework.stereotype.Component;

@Component("${dispatch.strategy-names.app-push}")
public class NotificationPushAdapter implements NotificationDispatchPort {

    @Override
    public void dispatch(Noti alarm) {
        // TODO Firebase Cloud Messaging(FCM) 푸시 알림 전송 로직 구현
    }
}
