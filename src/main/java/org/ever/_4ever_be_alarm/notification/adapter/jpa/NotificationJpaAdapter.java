package org.ever._4ever_be_alarm.notification.adapter.jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.ChannelRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationChannelRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationErrorCodeRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationLogRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationStatusRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationTargetRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationTemplateRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.SourceRepository;
import org.ever._4ever_be_alarm.notification.domain.model.Notification;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationRepositoryPort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationJpaAdapter implements NotificationRepositoryPort {

    private final ChannelRepository channelRepository;
    private final NotificationChannelRepository notificationChannelRepository;
    private final NotificationErrorCodeRepository notificationErrorCodeRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository notificationStatusRepository;
    private final NotificationTargetRepository notificationTargetRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final SourceRepository sourceRepository;
//    private final NotificationMapper notificationMapper;

    @Override
    public Notification save(Notification alarm) {
        return null;
    }

    @Override
    public List<Notification> findByUserId(String userId) {
        return List.of();
    }
}
