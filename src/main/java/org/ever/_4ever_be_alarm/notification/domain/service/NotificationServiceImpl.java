package org.ever._4ever_be_alarm.notification.domain.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.ever._4ever_be_alarm.common.response.PageResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationCountResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationReadResponseDto;
import org.ever._4ever_be_alarm.notification.domain.model.Noti;
import org.ever._4ever_be_alarm.notification.domain.model.constants.ReferenceTypeEnum;
import org.ever._4ever_be_alarm.notification.domain.model.constants.SourceTypeEnum;
import org.ever._4ever_be_alarm.notification.domain.port.in.NotificationQueryUseCase;
import org.ever._4ever_be_alarm.notification.domain.port.in.NotificationSendUseCase;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationRepositoryPort;
import org.ever.event.AlarmEvent;
import org.ever.event.StatusEvent;
import org.ever.event.alarm.TargetType;
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

//    @Override
//    public void sendNotification(AlarmSentEvent event) {
//        // TODO: 알림 전송 로직 구현
//        NotificationDispatchPort sseEmitter = notificationDispatchAdapters.get(SSE_STRATEGY_NAME);
//
//        if (event.getTargetId().isEmpty()) {
//            NotificationDispatchPort pushAlarm = notificationDispatchAdapters.get(
//                APP_PUSH_STRATEGY_NAME);
//            // TODO: Push 알림 전송 로직 구현
//        }
//        // TODO: SSE 알림 전송 로직 구현
//    }

    @Transactional
    @Override
    public UUID createNotification(AlarmEvent event) {
        // TODO: 알림 생성 로직 구현

        try {
            UUID alarmId = UUID.fromString(event.getAlarmId());
            UUID targetId = UUID.fromString(event.getTargetId());
            UUID linkId = UUID.fromString(event.getLinkId());

            Noti notification = Noti.builder()
                .id(alarmId)
                .targetId(targetId)
                .targetType(event.getTargetType())
                .title(event.getTitle())
                .message(event.getMessage())
                .referenceId(linkId)
                .referenceType(ReferenceTypeEnum.fromString(event.getLinkType().name()))
                .source(SourceTypeEnum.fromString(event.getSource()))
                .build();

            // 대상 설정 -> EMP, DEP, CUS, SUP
            if (event.getTargetType() == TargetType.DEPARTMENT) { // TODO 부서 구성원 가져와서 각각 처리
                List<UUID> memberIds = List.of(); // 부서 구성원 ID 리스트 가져오기
                for (UUID memberId : memberIds) {
                    // 각 구성원별로 알림 생성 로직 구현
                }

                return null;
            }

            // 알림 저장
            Noti result = notificationRepository.save(notification);
            // 알림 발송
            dispatchNotification(result);

            return result.getId();
        } catch (IllegalArgumentException | NullPointerException e) {
            // TODO 알림 생성 실패 처리 로직 구현
            return null;
        } catch (Exception e) {
            // TODO 알림 생성 기타 예외 처리 로직 구현
            return null;
        }
    }

    /**
     * 알림 발송
     */
    public void dispatchNotification(Noti notification) {

        if (notification.getTargetType() == TargetType.CUSTOMER
            || notification.getTargetType() == TargetType.SUPPLIER) {
            NotificationDispatchPort pushAlarm = notificationDispatchAdapters
                .get(APP_PUSH_STRATEGY_NAME);

            pushAlarm.dispatch(notification);
        }

        NotificationDispatchPort sseEmitter = notificationDispatchAdapters
            .get(SSE_STRATEGY_NAME);

        sseEmitter.dispatch(notification);
    }

    @Transactional
    @Override
    public void updateNotificationStatus(StatusEvent event) {
        // TODO: 알림 상태 업데이트 로직 구현
//        notificationRepository.updateNotificationStatus(event.getEventId(), event.isSuccess());
    }


}
