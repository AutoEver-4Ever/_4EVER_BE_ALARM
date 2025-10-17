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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationLog extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "error_code_id", nullable = false)
    private NotificationErrorCode notificationErrorCode;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Builder
    public NotificationLog(Notification notification, NotificationErrorCode notificationErrorCode,
        Integer retryCount) {
        this.notification = notification;
        this.notificationErrorCode = notificationErrorCode;
        this.retryCount = retryCount;
    }

    @Builder
    public NotificationLog(Notification notification, NotificationErrorCode notificationErrorCode) {
        this.notification = notification;
        this.notificationErrorCode = notificationErrorCode;
        this.retryCount = 0;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }
}
