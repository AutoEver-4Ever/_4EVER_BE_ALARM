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
@Table(name = "notification_error_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationErrorCode extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "error_code", nullable = false)
    private Integer errorCode;

    @Column(name = "error_message", nullable = false, length = 100)
    private String errorMessage;

    // @OneToMany(mappedBy = "notificationErrorCode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<NotificationLog> notificationLogs = new ArrayList<>();

    @Builder
    public NotificationErrorCode(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
