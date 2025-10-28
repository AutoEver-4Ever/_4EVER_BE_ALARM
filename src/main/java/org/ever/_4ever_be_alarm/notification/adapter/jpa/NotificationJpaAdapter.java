package org.ever._4ever_be_alarm.notification.adapter.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.common.response.PageDto;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.Notification;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.NotificationStatus;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.NotificationTarget;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.Source;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.ChannelRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationChannelRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationErrorCodeRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationLogRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationStatusRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationTargetRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationTemplateRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.SourceRepository;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationCountResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationReadResponseDto;
import org.ever._4ever_be_alarm.notification.domain.model.Noti;
import org.ever._4ever_be_alarm.notification.domain.model.constants.NotificationStatusEnum;
import org.ever._4ever_be_alarm.notification.domain.model.constants.SourceTypeEnum;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    @Transactional
    public Noti save(Noti alarm) {
        // TODO Notification(domain) -> Entity 변환 후 저장 로직 구현

        Optional<Source> tempSource = sourceRepository.findBySourceName(
            SourceTypeEnum.fromString(alarm.getSource().name()));

        Source source;
        if (tempSource.isPresent()) {
            source = tempSource.get();
        } else {
            // Source 생성 및 저장을 하면 안되는 이슈가 있어서 주석 처리
//            Source newSource = Source.builder()
//                .sourceName(SourceTypeEnum.fromString(alarm.getSource().name()))
//                .build();
//            source = sourceRepository.save(newSource);
            throw new IllegalStateException("Source를 찾을 수 없습니다."); // TODO 예외 처리 개선
        }

        Notification notification = Notification.builder()
            .id(alarm.getId())
            .title(alarm.getTitle())
            .message(alarm.getMessage())
            .referenceId(alarm.getReferenceId())
            .referenceType(alarm.getReferenceType())
            .source(source)
            .sendAt(LocalDateTime.now())
            .scheduledAt(Optional.ofNullable(alarm.getScheduledAt()).orElse(LocalDateTime.now()))
            .build();

        notificationRepository.save(notification);

        NotificationStatus notificationStatus = notificationStatusRepository.findByStatusName(
            NotificationStatusEnum.PENDING).get();

        NotificationTarget target = NotificationTarget.builder()
            .notification(notification)
            .notificationStatus(notificationStatus)
            .userId(alarm.getTargetId())
            .build();

        notificationTargetRepository.save(target);

        return Noti.builder()
            .id(notification.getId())
            .targetId(target.getUserId())
            .targetType(alarm.getTargetType())
            .title(notification.getTitle())
            .message(notification.getMessage())
            .referenceId(notification.getReferenceId())
            .referenceType(notification.getReferenceType())
            .source(SourceTypeEnum.fromString(notification.getSource().getSourceName().name()))
            .sendAt(notification.getSendAt())
            .scheduledAt(notification.getScheduledAt())
            .build();
    }

    @Override
    public List<Noti> findByUserId(String userId) {
        return List.of();
    }

    @Override
    public PageResponseDto<NotificationListResponseDto> getNotificationList(
        UUID userId,
        String sortBy,
        String order,
        SourceTypeEnum source,
        int page, int size
    ) {
        // sortBy는 기본값 "createdAt", 나중에 동적으로 변경 가능하도록 파라미터 유지
        String sortField = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;

        // order는 기본값 DESC, asc가 명시적으로 지정된 경우만 ASC
        Sort.Direction direction = "asc".equalsIgnoreCase(order)
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Source 필터링: null이거나 UNKNOWN이면 전체 조회, 그 외에는 해당 source만 조회
        Page<NotificationTarget> notificationTargets;
        if (source == null || source == SourceTypeEnum.UNKNOWN) {
            notificationTargets = notificationTargetRepository.findByUserId(userId, pageable);
        } else {
            notificationTargets = notificationTargetRepository.findByUserIdAndSource(
                userId, source.name(), pageable);
        }

        // NotificationListResponseDto로 변환
        List<NotificationListResponseDto> items = notificationTargets.getContent().stream()
            .map(this::toNotificationListResponseDto)
            .collect(Collectors.toList());

        // PageResponseDto 생성
        return PageResponseDto.<NotificationListResponseDto>builder()
            .items(items)
            .page(PageDto.builder()
                .number(notificationTargets.getNumber())
                .size(notificationTargets.getSize())
                .totalElements((int) notificationTargets.getTotalElements())
                .totalPages(notificationTargets.getTotalPages())
                .hasNext(notificationTargets.hasNext())
                .build())
            .build();
    }

    @Override
    public NotificationCountResponseDto countUnreadByUserId(UUID userId) {
        return NotificationCountResponseDto.builder()
            .count(Math.toIntExact(notificationTargetRepository.countUnreadByUserId(userId)))
            .build();
    }

    @Override
    public NotificationCountResponseDto countByUserId(UUID userId) {
        return NotificationCountResponseDto.builder()
            .count(Math.toIntExact(notificationTargetRepository.countByUserId(userId)))
            .build();
    }

    @Override
    public NotificationCountResponseDto countByUserIdAndStatus(UUID userId, Boolean isRead) {
        return NotificationCountResponseDto.builder()
            .count(Math.toIntExact(
                notificationTargetRepository.countByUserIdAndIsRead(userId, isRead)))
            .build();
    }

    @Override
    @Transactional
    public NotificationReadResponseDto markAsReadList(UUID userId, List<UUID> notificationIds) {
        int totalProcessed = 0;
        LocalDateTime readAt = LocalDateTime.now();

        for (UUID notificationId : notificationIds) {
            int updated = notificationTargetRepository
                .markAsReadByUserIdAndNotificationId(userId, notificationId, readAt);
            totalProcessed += updated;
        }

        return NotificationReadResponseDto.builder()
            .processedCount(totalProcessed)
            .build();
    }

    @Override
    @Transactional
    public NotificationReadResponseDto markAsReadAll(UUID userId) {
        LocalDateTime readAt = LocalDateTime.now();
        int totalProcessed = notificationTargetRepository.markAllAsReadByUserId(userId, readAt);

        return NotificationReadResponseDto.builder()
            .processedCount(totalProcessed)
            .build();
    }

    @Override
    @Transactional
    public NotificationReadResponseDto markAsRead(UUID userId, UUID notificationId) {
        LocalDateTime readAt = LocalDateTime.now();
        int updated = notificationTargetRepository
            .markAsReadByUserIdAndNotificationId(userId, notificationId, readAt);
        return NotificationReadResponseDto.builder()
            .processedCount(updated)
            .build();
    }

    private NotificationListResponseDto toNotificationListResponseDto(NotificationTarget target) {
        var notification = target.getNotification();
        return NotificationListResponseDto.builder()
            .notificationId(notification.getId().toString())
            .notificationTitle(notification.getTitle())
            .notificationMessage(notification.getMessage())
            .linkType(
                notification.getReferenceType() != null
                    ? notification.getReferenceType().toString()
                    : null
            )
            .linkId(notification.getReferenceId() != null
                ? notification.getReferenceId().toString()
                : null
            )
            .source(String.valueOf(notification.getSource().getSourceName()))
            .createdAt(notification.getCreatedAt())
            .isRead(target.getIsRead())
            .build();
    }
}
