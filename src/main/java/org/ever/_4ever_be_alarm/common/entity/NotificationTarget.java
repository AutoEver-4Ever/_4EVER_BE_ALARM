package org.ever._4ever_be_alarm.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_target")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationTarget extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private NotificationStatus notificationStatus;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Builder
    public NotificationTarget(Notification notification, NotificationStatus notificationStatus,
        Long userId, Boolean isRead, LocalDateTime readAt) {
        this.notification = notification;
        this.notificationStatus = notificationStatus;
        this.userId = userId;
        this.isRead = isRead;
        this.readAt = readAt;
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public void markAsUnread() {
        this.isRead = false;
        this.readAt = null;
    }
}
