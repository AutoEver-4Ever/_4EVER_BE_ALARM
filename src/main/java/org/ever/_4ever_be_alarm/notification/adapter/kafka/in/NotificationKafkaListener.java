package org.ever._4ever_be_alarm.notification.adapter.kafka.in;

import static org.ever._4ever_be_alarm.infrastructure.kafka.config.KafkaTopicConfig.ALARM_REQUEST_STATUS_TOPIC;
import static org.ever._4ever_be_alarm.infrastructure.kafka.config.KafkaTopicConfig.ALARM_REQUEST_TOPIC;
import static org.ever._4ever_be_alarm.infrastructure.kafka.config.KafkaTopicConfig.ALARM_SENT_STATUS_TOPIC;
import static org.ever._4ever_be_alarm.infrastructure.kafka.config.KafkaTopicConfig.ALARM_SENT_TOPIC;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ever._4ever_be_alarm.notification.domain.port.in.NotificationSendUseCase;
import org.ever.event.AlarmEvent;
import org.ever.event.AlarmSentEvent;
import org.ever.event.StatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaListener {

    private final NotificationSendUseCase notificationSendUseCase;

    /**
     * Alarm 외 서버에서 Alarm Request 이벤트 리스너
     */
    @KafkaListener(
        topics = ALARM_REQUEST_TOPIC,
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleAlarmRequest(
        @Payload AlarmEvent event,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        Acknowledgment acknowledgment
    ) {
        try {
            log.info("알람 요청 이벤트 수신 - Topic: {}, Partition: {}, Offset: {}, AlarmId: {}",
                topic, partition, offset, event.getAlarmId());

            // 알람 요청 처리 로직
            UUID notificationId = notificationSendUseCase.createNotification(event);

            // 수동 커밋
            acknowledgment.acknowledge();
            log.info("알람 요청 이벤트 처리 완료 - AlarmId: {}", event.getAlarmId());

        } catch (Exception e) {
            log.error("알람 요청 이벤트 처리 실패 - AlarmId: {}", event.getAlarmId(), e);
            // 에러 처리 로직
            // acknowledgment를 호출하지 않으면 메시지가 재처리됨
        }
    }

    /**
     * 알림 발송 요청 결과 이벤트 리스너
     */
    @KafkaListener(
        topics = ALARM_SENT_STATUS_TOPIC,
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNotificationStatus(
        @Payload StatusEvent event,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        Acknowledgment acknowledgment
    ) {
        try {
            log.info("알람 발송 결과 이벤트 수신 - Topic: {}, Partition: {}, Offset: {}, eventId: {}",
                topic, partition, offset, event.getEventId());

            // TODO 알람 발송 실패 처리 로직

            acknowledgment.acknowledge();
            log.info("알람 발송 실패 이벤트 처리 완료 - eventId: {}", event.getEventId());

        } catch (Exception e) {
            log.error("알람 발송 실패 이벤트 처리 실패 - eventId: {}", event.getEventId(), e);
            // 에러 처리 로직
            // acknowledgment를 호출하지 않으면 메시지가 재처리됨
        }
    }

    /**
     * TODO : ALARM -> ALARM 외 서버
     * 현재는 불필요한 상황 알림이 중요한 서비스는 아니기 때문에 구현 보류
     *
     * Alarm 외 서버에서 Alarm Request의 결과 이벤트 리스너
     */
    @KafkaListener(
        topics = ALARM_REQUEST_STATUS_TOPIC,
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleAlarmRequestResult(
        @Payload StatusEvent event,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        Acknowledgment acknowledgment
    ) {
        try {
            log.info(
                "알람 요청 결과 이벤트 수신 - Topic: {}, Partition: {}, Offset: {}, eventId: {}",
                topic, partition, offset, event.getEventId());

            // 알람 요청 결과 처리 로직

            // 수동 커밋
            acknowledgment.acknowledge();
            log.info("알람 요청 결과 처리 완료 - eventId: {}", event.getEventId());

        } catch (Exception e) {
            log.error("알람 요청 이벤트 처리 실패 - eventId: {}", event.getEventId(), e);
            // 에러 처리 로직
            // acknowledgment를 호출하지 않으면 메시지가 재처리됨
        }
    }


    /**
     * TODO : ALARM -> GW
     *
     * 알림 발송 요청 이벤트 리스너
     */
    @KafkaListener(
        topics = ALARM_SENT_TOPIC,
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNotificationRequest(
        @Payload AlarmSentEvent event,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, // 기본 제공 : 토픽 이름
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition, // 기본 제공 : 파티션 번호
        @Header(KafkaHeaders.OFFSET) long offset, // 기본 제공 : 오프셋 번호
        Acknowledgment acknowledgment
    ) {
        try {
            log.info("알림 발송 이벤트 수신 - Topic: {}, Partition: {}, Offset: {}, eventId: {}",
                topic, partition, offset, event.getEventId());

            // TODO 알림 발송

            log.info("알림 발송 완료 - eventId: {}", event.getEventId());

            // 수동 커밋
            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("알림 발송 이벤트 처리 실패 - eventId: {}", event.getEventId(), e);
            // 에러 처리 로직
            // acknowledgment를 호출하지 않으면 메시지가 재처리됨
        }
    }

}
