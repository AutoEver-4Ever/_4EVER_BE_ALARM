package org.ever._4ever_be_alarm.notification.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.ever._4ever_be_alarm.notification.domain.model.constants.ReferenceTypeEnum;

@Data
@Builder
@Getter
public class Notification {

    private UUID id;
    private String title;
    private String message;
    private UUID referenceId;
    private ReferenceTypeEnum referenceType;
    private UUID sourceId;
    private LocalDateTime sendAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}