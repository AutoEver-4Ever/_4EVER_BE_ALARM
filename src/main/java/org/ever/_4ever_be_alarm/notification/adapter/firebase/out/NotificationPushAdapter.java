package org.ever._4ever_be_alarm.notification.adapter.firebase.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ever._4ever_be_alarm.notification.domain.model.Noti;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("${dispatch.strategy-names.app-push}")
public class NotificationPushAdapter implements NotificationDispatchPort {

    @Override
    public void dispatch(Noti alarm) {
        log.info("[DISPATCH-PUSH] 푸시 알림 전송 시작 - NotificationId: {}, Title: {}",
            alarm.getId(), alarm.getTitle());

        try {
            // TODO: Firebase Cloud Messaging(FCM) 푸시 알림 전송 로직 구현
            log.debug("[DISPATCH-PUSH] FCM 전송 로직은 아직 구현되지 않음 - NotificationId: {}",
                alarm.getId());
            log.info("[DISPATCH-PUSH] 푸시 알림 전송 완료 (mock) - NotificationId: {}",
                alarm.getId());

        } catch (Exception e) {
            log.error("[DISPATCH-PUSH] 푸시 알림 전송 실패 - NotificationId: {}, Error: {}",
                alarm.getId(), e.getMessage(), e);
            throw new RuntimeException("푸시 알림 전송 실패", e);
        }
    }
}
