package org.ever._4ever_be_alarm.notification.entity;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ever._4ever_be_alarm.common.entity.TimeStamp;

@Entity
@Table(name = "notification_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationStatus extends TimeStamp {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "status_name", nullable = false, unique = true, length = 100)
    private String statusName;

//    @OneToMany(mappedBy = "notificationStatus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private final List<NotificationTarget> notificationTargets = new ArrayList<>();

    @Builder
    public NotificationStatus(String statusName) {
        this.statusName = statusName;
    }

    @PrePersist
    public void perPersist() {
        if (this.id == null) {
            this.id = Generators.timeBasedEpochGenerator().generate();
        }
    }
}