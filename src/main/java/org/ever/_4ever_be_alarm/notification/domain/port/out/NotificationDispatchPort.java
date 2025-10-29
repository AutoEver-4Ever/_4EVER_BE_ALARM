package org.ever._4ever_be_alarm.notification.domain.port.out;

import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.Notification;

public interface NotificationDispatchPort {

    void dispatch(Notification alarm); // 예: 푸시 알림, 이메일, SMS 등
}
