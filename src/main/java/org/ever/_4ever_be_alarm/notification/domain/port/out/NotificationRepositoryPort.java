package org.ever._4ever_be_alarm.notification.domain.port.out;

import java.util.List;
import org.ever._4ever_be_alarm.notification.domain.model.Notification;

public interface NotificationRepositoryPort {

    Notification save(Notification alarm);

    List<Notification> findByUserId(String userId);
}
