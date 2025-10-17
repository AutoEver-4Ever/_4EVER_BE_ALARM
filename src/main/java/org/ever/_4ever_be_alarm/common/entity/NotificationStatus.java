package org.ever._4ever_be_alarm.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationStatus extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status_name", nullable = false, length = 100)
    private String statusName;

    // @OneToMany(mappedBy = "notificationStatus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<NotificationTarget> notificationTargets = new ArrayList<>();

    @Builder
    public NotificationStatus(String statusName) {
        this.statusName = statusName;
    }
}
