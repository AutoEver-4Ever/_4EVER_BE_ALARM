package org.ever._4ever_be_alarm.notification.domain.port.out;

import java.util.List;
import java.util.UUID;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.domain.model.Notification;

public interface NotificationRepositoryPort {

    Notification save(Notification alarm);

    List<Notification> findByUserId(String userId);

    PageResponseDto<NotificationListResponseDto> findNotificationTargetsByUserId(
        UUID userId, String sortBy, String order, String source, int page, int size
    );

    Integer countUnreadByUserId(UUID userId);

    Integer markAsReadList(UUID userId, List<UUID> notificationIds);

    Integer markAsReadAll(UUID userId);

    Boolean markAsRead(UUID userId, String notificationId);
}
