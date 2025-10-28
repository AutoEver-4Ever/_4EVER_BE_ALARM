package org.ever.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.ever.event.alarm.AlarmType;
import org.ever.event.alarm.LinkType;
import org.ever.event.alarm.TargetType;

/**
 * 알림 발송 이벤트 클래스
 * 알림 서버에서 클라이언트로 알림을 전송하는 경우에 사용한다.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmSentEvent extends BaseEvent {

    String alarmId;
    AlarmType alarmType;            // source와 구분하기 위해 notificationType으로 명명
    String targetId;
    TargetType targetType;
    String title;
    String message;
    String linkId;
    LinkType linkType;
}
