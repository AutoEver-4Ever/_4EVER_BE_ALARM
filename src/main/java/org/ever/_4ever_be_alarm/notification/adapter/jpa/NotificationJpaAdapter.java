package org.ever._4ever_be_alarm.notification.adapter.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.common.response.PageDto;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.NotificationTarget;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.ChannelRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationChannelRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationErrorCodeRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationLogRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationStatusRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationTargetRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationTemplateRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.SourceRepository;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.domain.model.Notification;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public PageResponseDto<NotificationListResponseDto> findNotificationTargetsByUserId(
        UUID userId,
        String sortBy,
        String order,
        String source,
        int page, int size
    ) {
        // 정렬 설정
        Sort sort = "asc".equalsIgnoreCase(order) ? Sort.by(Sort.Direction.ASC, "createdAt")
            : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        // NotificationTarget 조회
        Page<NotificationTarget> notificationTargets = notificationTargetRepository.findByUserId(
            userId, pageable);

        // Source 필터 적용
        List<NotificationTarget> filteredTargets = notificationTargets.getContent();
        if (source != null && !source.isBlank()) {
            filteredTargets = filteredTargets.stream()
                .filter(
                    nt -> source.equalsIgnoreCase(nt.getNotification().getSource().getSourceName()))
                .collect(Collectors.toList());
        }

        // NotificationListResponseDto로 변환
        List<NotificationListResponseDto> items = filteredTargets.stream()
            .map(this::toNotificationListResponseDto)
            .collect(Collectors.toList());

        // PageDto 생성
        PageDto pageDto = PageDto.builder()
            .number(notificationTargets.getNumber())
            .size(notificationTargets.getSize())
            .totalElements((int) notificationTargets.getTotalElements())
            .totalPages(notificationTargets.getTotalPages())
            .hasNext(notificationTargets.hasNext())
            .build();

        return PageResponseDto.<NotificationListResponseDto>builder()
            .items(items)
            .page(pageDto)
            .build();
    }

    @Override
    public Integer countUnreadByUserId(UUID userId) {
        return Math.toIntExact(notificationTargetRepository.countUnreadByUserId(userId));
    }

    @Override
    @Transactional
    public Integer markAsReadList(UUID userId, List<UUID> notificationIds) {
        int totalProcessed = 0;
        LocalDateTime readAt = LocalDateTime.now();

        for (UUID notificationId : notificationIds) {
            try {
                int updated = notificationTargetRepository.markAsReadByUserIdAndNotificationId(
                    userId, notificationId, readAt);
                totalProcessed += updated;
            } catch (IllegalArgumentException e) {
                // 잘못된 UUID 형식은 무시하고 계속 진행
            }
        }

        return totalProcessed;
    }

    @Override
    @Transactional
    public Integer markAsReadAll(UUID userId) {
        LocalDateTime readAt = LocalDateTime.now();
        return notificationTargetRepository.markAllAsReadByUserId(userId, readAt);
    }

    @Override
    @Transactional
    public Boolean markAsRead(UUID userId, String notificationId) {
        try {
            UUID notificationUuid = UUID.fromString(notificationId);
            LocalDateTime readAt = LocalDateTime.now();
            int updated = notificationTargetRepository.markAsReadByUserIdAndNotificationId(userId,
                notificationUuid, readAt);
            return updated > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private NotificationListResponseDto toNotificationListResponseDto(NotificationTarget target) {
        var notification = target.getNotification();
        return NotificationListResponseDto.builder()
            .notificationId(notification.getId().toString())
            .notificationTitle(notification.getTitle())
            .notificationMessage(notification.getMessage())
            .linkType(
                notification.getReferenceType() != null ? notification.getReferenceType().toString()
                    : null)
            .linkId(notification.getReferenceId() != null ? notification.getReferenceId().toString()
                : null)
            .source(notification.getSource().getSourceName())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
