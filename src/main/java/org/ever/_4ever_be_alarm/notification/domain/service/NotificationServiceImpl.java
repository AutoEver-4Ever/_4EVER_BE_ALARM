package org.ever._4ever_be_alarm.notification.domain.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationCountResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationReadResponseDto;
import org.ever._4ever_be_alarm.notification.domain.model.constants.SourceTypeEnum;
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
    public PageResponseDto<NotificationListResponseDto> getNotificationPage(
        UUID userId,
        String sortBy,
        String order,
        String source,
        int page, int size
    ) {
        SourceTypeEnum sourceType = SourceTypeEnum.fromString(source);

        return notificationRepository
            .getNotificationList(userId, sortBy, order, sourceType, page, size);
    }

    @Transactional(readOnly = true)
    @Override
    public NotificationCountResponseDto getNotificationCount(UUID userId, String status) {
        if (status == null || status.isEmpty()) { // 전체 카운트
            return notificationRepository.countByUserId(userId);
        } else if (status.equalsIgnoreCase("READ")) { // 읽음 카운트
            return notificationRepository.countByUserIdAndStatus(userId, true);
        } else if (status.equalsIgnoreCase("UNREAD")) { // 안읽음 카운트
            return notificationRepository.countByUserIdAndStatus(userId, false);
        } else { // 잘못된 상태 값
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    @Transactional
    @Override
    public NotificationReadResponseDto markAsReadList(UUID userId, List<UUID> notificationIds) {
        return notificationRepository.markAsReadList(userId, notificationIds);
    }

    @Transactional
    @Override
    public NotificationReadResponseDto markAsReadAll(UUID userId) {
        return notificationRepository.markAsReadAll(userId);
    }

    @Transactional
    @Override
    public NotificationReadResponseDto markAsReadOne(UUID userId, UUID notificationId) {
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
