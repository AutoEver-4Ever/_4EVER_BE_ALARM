package org.ever._4ever_be_alarm.notification.domain.port.in;


import java.util.List;
import org.ever._4ever_be_alarm.notification.domain.model.Notification;

public interface NotificationQueryUseCase {

    List<Notification> getNotifications(String userId, int page, int size);

    Integer getUnreadCount(String userId);

    Boolean markAsRead(String userId, String notificationId);

}
