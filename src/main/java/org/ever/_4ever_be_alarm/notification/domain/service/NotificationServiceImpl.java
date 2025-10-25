package org.ever._4ever_be_alarm.notification.domain.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.notification.domain.model.Notification;
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

    @Transactional
    @Override
    public List<Notification> getNotifications(String userId, int page, int size) {
        return List.of();
    }

    @Transactional
    @Override
    public Integer getUnreadCount(String userId) {
        return 0;
    }

    @Transactional
    @Override
    public Boolean markAsRead(String userId, String notificationId) {
        return null;
    }

    @Override
    public void sendNotification(String userId, String title, String message) {

        NotificationDispatchPort sseEmitter = notificationDispatchAdapters.get(SSE_STRATEGY_NAME);

        if (userId.isEmpty()) {
            NotificationDispatchPort pushAlarm = notificationDispatchAdapters.get(
                PUSH_STRATEGY_NAME);
        }

    }
}
