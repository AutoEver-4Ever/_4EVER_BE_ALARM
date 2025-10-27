package org.ever._4ever_be_alarm.notification.domain.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.domain.port.in.NotificationQueryUseCase;
import org.ever._4ever_be_alarm.notification.domain.port.in.NotificationSendUseCase;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationQueryUseCase, NotificationSendUseCase {

    private final NotificationRepositoryPort notificationRepository;
    private final Map<String, NotificationDispatchPort> notificationDispatchAdapters;
    @Value("${dispatch.strategy-names.sse}")
    private String SSE_STRATEGY_NAME;
    @Value("${dispatch.strategy-names.push}")
    private String PUSH_STRATEGY_NAME;

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<NotificationListResponseDto> getNotifications(
        UUID userId,
        String sortBy,
        String order,
        String source,
        int page, int size
    ) {
        return notificationRepository
            .findNotificationTargetsByUserId(userId, sortBy, order, source, page, size);
    }

    @Transactional(readOnly = true)
    @Override
    public Integer getUnreadCount(UUID userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public Integer getNotificationCount(UUID userId, String status) {
        return 0;
    }

    @Transactional
    @Override
    public Integer markAsReadList(UUID userId, List<UUID> notificationIds) {
        return notificationRepository.markAsReadList(userId, notificationIds);
    }

    @Transactional
    @Override
    public Integer markAsReadAll(UUID userId) {
        return notificationRepository.markAsReadAll(userId);
    }

    @Transactional
    @Override
    public Boolean markAsReadOne(UUID userId, String notificationId) {
        return notificationRepository.markAsRead(userId, notificationId);
    }

    @Override
    public void sendNotification(String userId, String title, String message) {
        // TODO: 알림 전송 로직 구현
        NotificationDispatchPort sseEmitter = notificationDispatchAdapters.get(SSE_STRATEGY_NAME);

        if (userId.isEmpty()) {
            NotificationDispatchPort pushAlarm = notificationDispatchAdapters.get(
                PUSH_STRATEGY_NAME);
            // TODO: Push 알림 전송 로직 구현
        }
        // TODO: SSE 알림 전송 로직 구현
    }
}
