package org.ever._4ever_be_alarm.notification.domain.port.config;

import org.ever._4ever_be_alarm.notification.adapter.firebase.out.NotificationPushAdapter;
import org.ever._4ever_be_alarm.notification.adapter.kafka.out.NotificationEventProducerAdapter;
import org.ever._4ever_be_alarm.notification.domain.port.out.NotificationDispatchPort;
import org.ever._4ever_be_alarm.notification.domain.port.out.strategy.DispatchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class NotificationDispatchConfig {

    /**
     * SSE 발송 전략 빈 정의
     */
    @Bean(name = "sse")
    public NotificationDispatchPort sseDispatchPort(KafkaTemplate<String, Object> kafkaTemplate) {
        return new NotificationEventProducerAdapter(kafkaTemplate);
    }

    /**
     * APP PUSH 발송 전략 빈 정의
     */
    @Bean(name = "app_push")
    public NotificationDispatchPort pushDispatchPort(/* FirebaseAdminSdk firebaseSdk */) {
        // return new NotificationPushAdapter(firebaseSdk);
        return new NotificationPushAdapter(); // 현재 코드 기준으로는 의존성 없음
    }

    // 참고: 빈 이름은 DispatchStrategy의 beanName 값과 일치해야 합니다.
    // DispatchStrategy.SSE.getBeanName() => "sse"
    // DispatchStrategy.APP_PUSH.getBeanName() => "app_push"
}
