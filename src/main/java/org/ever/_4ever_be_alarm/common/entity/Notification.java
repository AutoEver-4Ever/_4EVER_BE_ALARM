package org.ever._4ever_be_alarm.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_type_id", nullable = false)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_type_id", nullable = false)
    private ChannelType channelType;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "sent_at")
    private LocalDateTime sentAt; // 예약 발송이 되는건가?

    // @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<NotificationTarget> notificationTargets = new ArrayList<>();

    // @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<NotificationChannel> notificationChannels = new ArrayList<>();

    // @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<NotificationLog> notificationLogs = new ArrayList<>();

    @Builder
    public Notification(NotificationType notificationType, ChannelType channelType, String title,
        String message, LocalDateTime sentAt) {
        this.notificationType = notificationType;
        this.channelType = channelType;
        this.title = title;
        this.message = message;
        this.sentAt = sentAt;
    }

    public void updateSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
