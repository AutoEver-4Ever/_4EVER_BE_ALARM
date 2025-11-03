package org.ever._4ever_be_alarm.notification.adapter.firebase.out;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ever._4ever_be_alarm.notification.domain.model.Noti;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;

@Slf4j
@RequiredArgsConstructor
public class NotificationPushAdapter implements NotificationDispatchPort {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void dispatch(Noti alarm) {
        log.info("[DISPATCH-PUSH] 푸시 알림 준비 - NotificationId: {}, TargetId: {}, Title: {}",
            alarm.getId(), alarm.getTargetId(), alarm.getTitle());

        // Noti 도메인 모델에 fcmToken이 포함되어야 함
        // NotificationServiceImpl에서 token 조회 후 Noti 객체에 포함시켜서 전달받음
        String fcmToken = alarm.getFcmToken();

        if (fcmToken == null || fcmToken.isBlank()) {
            log.warn(
                "[DISPATCH-PUSH] FCM 토큰이 없어 푸시 알림을 전송할 수 없습니다. - NotificationId: {}, TargetId: {}",
                alarm.getId(), alarm.getTargetId());
            return;
        }

        try {
            // FCM Notification 생성
            Notification notification = Notification.builder()
                .setTitle(alarm.getTitle())
                .setBody(alarm.getMessage())
                .build();

            // 추가 데이터 구성
            Map<String, String> data = new HashMap<>();
            data.put("notificationId", alarm.getId() != null ? alarm.getId().toString() : "");
            data.put("referenceType",
                alarm.getReferenceType() != null ? alarm.getReferenceType().name() : "");
            data.put("referenceId",
                alarm.getReferenceId() != null ? alarm.getReferenceId().toString() : "");
            data.put("source", alarm.getSource() != null ? alarm.getSource().name() : "");
            data.put("sendAt", alarm.getSendAt() != null ? alarm.getSendAt().toString() : "");

            // FCM Message 생성 (토큰 기반)
            Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putAllData(data)
                .build();

            log.info("[DISPATCH-PUSH] 푸시 알림 전송 시작 - NotificationId: {}, TargetId: {}, Token: {}",
                alarm.getId(), alarm.getTargetId(), maskToken(fcmToken));

            // FCM 전송
            String messageId = firebaseMessaging.send(message);

            log.info("[DISPATCH-PUSH] FCM 전송 성공 - NotificationId: {}, MessageId: {}, TargetId: {}",
                alarm.getId(), messageId, alarm.getTargetId());

        } catch (FirebaseMessagingException e) {
            handleFirebaseMessagingException(e, fcmToken, alarm);
        } catch (Exception e) {
            log.error("[DISPATCH-PUSH] 푸시 알림 전송 실패 - NotificationId: {}, TargetId: {}, Error: {}",
                alarm.getId(), alarm.getTargetId(), e.getMessage(), e);
            throw new RuntimeException("푸시 알림 전송 실패", e);
        }
    }

    /**
     * FirebaseMessagingException 처리
     */
    private void handleFirebaseMessagingException(
        FirebaseMessagingException e,
        String fcmToken,
        Noti alarm
    ) {
        String errorCode = e.getMessagingErrorCode() != null
            ? e.getMessagingErrorCode().name()
            : "UNKNOWN";

        log.error("[DISPATCH-PUSH] FCM 전송 실패 - NotificationId: {}, ErrorCode: {}, Error: {}",
            alarm.getId(), errorCode, e.getMessage());

        // 유효하지 않은 토큰 처리
        if ("UNREGISTERED".equals(errorCode) || "INVALID_ARGUMENT".equals(errorCode)) {
            log.warn(
                "[DISPATCH-PUSH] 유효하지 않은 FCM 토큰 - NotificationId: {}, Token: {}, ErrorCode: {}",
                alarm.getId(), maskToken(fcmToken), errorCode);
            // TODO: 토큰 삭제 또는 비활성화 처리
            // userDeviceTokenRepository.deleteByFcmToken(fcmToken);
        }

        throw new RuntimeException("FCM 전송 실패 - ErrorCode: " + errorCode, e);
    }

    /**
     * 토큰 마스킹 (로그용)
     */
    private String maskToken(String token) {
        if (token == null || token.length() < 10) {
            return "***";
        }
        return token.substring(0, 10) + "...";
    }
}
