package org.ever._4ever_be_alarm.notification.domain.port.in;

import java.util.List;
import java.util.UUID;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationCountResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationReadResponseDto;

public interface NotificationQueryUseCase {

    PageResponseDto<NotificationListResponseDto> getNotificationPage(
        UUID userId, String sortBy, String order, String source, int page, int size
    );

    NotificationCountResponseDto getNotificationCount(UUID userId, String status);

    NotificationReadResponseDto markAsReadList(UUID userId, List<UUID> notificationIds);

    NotificationReadResponseDto markAsReadAll(UUID userId);

    NotificationReadResponseDto markAsReadOne(UUID userId, UUID notificationId);
}
