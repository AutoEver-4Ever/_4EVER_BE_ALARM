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
    @Value("${dispatch.strategy-names.app-push}")
    private String APP_PUSH_STRATEGY_NAME;

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<NotificationListResponseDto> getNotificationPage(
        String userId,
        String sortBy,
        String order,
        String source,
        int page, int size
    ) {
        SourceTypeEnum sourceType = SourceTypeEnum.fromString(source);

        UUID userUuid = UUID.fromString(userId);

        return notificationRepository
            .getNotificationList(userUuid, sortBy, order, sourceType, page, size);
    }

    @Transactional(readOnly = true)
    @Override
    public NotificationCountResponseDto getNotificationCount(String userId, String status) {
        UUID userUuid = UUID.fromString(userId);

        if (status == null || status.isEmpty()) { // 전체 카운트
            return notificationRepository.countByUserId(userUuid);
        } else if (status.equalsIgnoreCase("READ")) { // 읽음 카운트
            return notificationRepository.countByUserIdAndStatus(userUuid, true);
        } else if (status.equalsIgnoreCase("UNREAD")) { // 안읽음 카운트
            return notificationRepository.countByUserIdAndStatus(userUuid, false);
        } else { // 잘못된 상태 값
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    @Transactional
    @Override
    public NotificationReadResponseDto markAsReadList(String userId, List<String> notificationIds) {
        UUID userUuid = UUID.fromString(userId);
        List<UUID> notificationUuidList = notificationIds.stream()
            .map(UUID::fromString)
            .toList();

        return notificationRepository.markAsReadList(userUuid, notificationUuidList);
    }

    @Transactional
    @Override
    public NotificationReadResponseDto markAsReadAll(String userId) {
        UUID userUuid = UUID.fromString(userId);

        return notificationRepository.markAsReadAll(userUuid);
    }

    @Transactional
    @Override
    public NotificationReadResponseDto markAsReadOne(String userId, String notificationId) {
        UUID userUuid = UUID.fromString(userId);
        UUID notificationUuid = UUID.fromString(notificationId);

        return notificationRepository.markAsRead(userUuid, notificationUuid);
    }

    @Override
    public void sendNotification(String userId, String title, String message) {
        // TODO: 알림 전송 로직 구현
        NotificationDispatchPort sseEmitter = notificationDispatchAdapters.get(SSE_STRATEGY_NAME);

        if (userId.isEmpty()) {
            NotificationDispatchPort pushAlarm = notificationDispatchAdapters.get(
                APP_PUSH_STRATEGY_NAME);
            // TODO: Push 알림 전송 로직 구현
        }
        // TODO: SSE 알림 전송 로직 구현
    }
}
