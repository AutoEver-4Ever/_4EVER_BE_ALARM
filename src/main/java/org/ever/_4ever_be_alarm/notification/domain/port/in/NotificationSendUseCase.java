package org.ever._4ever_be_alarm.notification.domain.port.in;

public interface NotificationSendUseCase {

    void sendNotification(String userId, String title, String message);

}
