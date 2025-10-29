package org.ever._4ever_be_alarm.notification.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.ever._4ever_be_alarm.notification.service.SseEmitterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Service
@Slf4j
public class SseEmitterServiceImpl implements SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Value("${sse.emitter.timeout:3600_000}") // 기본값 1시간
    private Long emitterTimeout;

    @Override
    public SseEmitter addEmitter(String userId) {
        SseEmitter emitter = new SseEmitter(emitterTimeout);
        emitters.put(userId, emitter); // 유저ID를 키로 SseEmitter 저장 고유한 UserID 필요

        emitter.onCompletion(() -> {
            emitters.remove(userId);
            log.info("SseEmitter completed and removed for userId: {}", userId);
        });

        emitter.onTimeout(() -> {
            emitters.remove(userId);
            log.info("SseEmitter timed out and removed for userId: {}", userId);
        });

        emitter.onError((e) -> {
            emitters.remove(userId);
            log.error("SseEmitter error for userId: {}", userId, e);
        });

        return null;
    }

    @Override
    public void removeEmitter(String userId) {

    }

    @Override
    public void sendEvent(String userId, String eventName, Object data) {
        if (emitters.containsKey(userId)) {
            SseEmitter emitter = emitters.get(userId);
            try {
                SseEventBuilder sseEventBuilder = SseEmitter.event()
                    .name(eventName)
                    .data(data);

                emitter.send(sseEventBuilder);

                log.info("Sent SSE event to user: {}, name: {}, data: {}", userId, eventName, data);
            } catch (IOException e) {
                emitter.completeWithError(e); // 트랜스미션 오류 시 완료 처리
//                emitter.complete();
                emitters.remove(userId);
                log.error("Error sending event to userId: {}, removing emitter", userId, e);
            }
        } else {
            log.warn("No SseEmitter found for userId: {}", userId);
        }
    }
}
