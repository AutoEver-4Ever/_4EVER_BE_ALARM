package org.ever._4ever_be_alarm.notification.domain.port.in;

import java.util.List;
import java.util.UUID;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;

public interface NotificationQueryUseCase {

    PageResponseDto<NotificationListResponseDto> getNotifications(UUID userId, String sortBy,
        String order, String source, int page, int size);

    Integer getUnreadCount(UUID userId);

    Integer getNotificationCount(UUID userId, String status);

    Integer markAsReadList(UUID userId, List<UUID> notificationIds);

    Integer markAsReadAll(UUID userId);

    Boolean markAsReadOne(UUID userId, String notificationId);
}
