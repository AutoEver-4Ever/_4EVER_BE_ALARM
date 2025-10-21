package org.ever._4ever_be_alarm.notification.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.ever._4ever_be_alarm.common.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 사용자별 알림 조회
     */
    @Query("SELECT n FROM Notification n " +
        "JOIN n.notificationTargets nt " +
        "WHERE nt.userId = :userId " +
        "ORDER BY n.createdAt DESC")
    Page<Notification> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 사용자별 읽지 않은 알림 조회
     */
    @Query("SELECT n FROM Notification n " +
        "JOIN n.notificationTargets nt " +
        "WHERE nt.userId = :userId AND nt.isRead = false " +
        "ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByUserId(@Param("userId") Long userId);

    /**
     * 특정 기간 내 알림 조회
     */
    @Query("SELECT n FROM Notification n " +
        "WHERE n.createdAt BETWEEN :startDate AND :endDate " +
        "ORDER BY n.createdAt DESC")
    List<Notification> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    /**
     * 알림 타입별 조회
     */
    @Query("SELECT n FROM Notification n " +
        "WHERE n.notificationType.id = :notificationTypeId " +
        "ORDER BY n.createdAt DESC")
    List<Notification> findByNotificationTypeId(
        @Param("notificationTypeId") Long notificationTypeId);

    /**
     * 채널 타입별 조회
     */
    @Query("SELECT n FROM Notification n " +
        "WHERE n.channelType.id = :channelTypeId " +
        "ORDER BY n.createdAt DESC")
    List<Notification> findByChannelTypeId(@Param("channelTypeId") Long channelTypeId);

    /**
     * 전송되지 않은 알림 조회
     */
    @Query("SELECT n FROM Notification n " +
        "WHERE n.sentAt IS NULL " +
        "ORDER BY n.createdAt ASC")
    List<Notification> findUnsentNotifications();

    /**
     * 사용자별 알림 개수 조회
     */
    @Query("SELECT COUNT(n) FROM Notification n " +
        "JOIN n.notificationTargets nt " +
        "WHERE nt.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);

    /**
     * 사용자별 읽지 않은 알림 개수 조회
     */
    @Query("SELECT COUNT(n) FROM Notification n " +
        "JOIN n.notificationTargets nt " +
        "WHERE nt.userId = :userId AND nt.isRead = false")
    Long countUnreadByUserId(@Param("userId") Long userId);
}
