package org.ever._4ever_be_alarm.notification.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.ever._4ever_be_alarm.notification.entity.NotificationTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTargetRepository extends JpaRepository<NotificationTarget, UUID> {

    /**
     * 사용자별 알림 대상 조회
     */
    @Query("SELECT nt FROM NotificationTarget nt " +
        "WHERE nt.userId = :userId " +
        "ORDER BY nt.createdAt DESC")
    Page<NotificationTarget> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    /**
     * 사용자별 읽지 않은 알림 대상 조회
     */
    @Query("SELECT nt FROM NotificationTarget nt " +
        "WHERE nt.userId = :userId AND nt.isRead = false " +
        "ORDER BY nt.createdAt DESC")
    List<NotificationTarget> findUnreadByUserId(@Param("userId") UUID userId);

    /**
     * 특정 알림의 모든 대상 조회
     */
    @Query("SELECT nt FROM NotificationTarget nt " +
        "WHERE nt.notification.id = :notificationId " +
        "ORDER BY nt.createdAt DESC")
    List<NotificationTarget> findByNotificationId(@Param("notificationId") UUID notificationId);

    /**
     * 특정 알림과 사용자의 대상 조회
     */
    @Query("SELECT nt FROM NotificationTarget nt " +
        "WHERE nt.notification.id = :notificationId AND nt.userId = :userId")
    Optional<NotificationTarget> findByNotificationIdAndUserId(
        @Param("notificationId") UUID notificationId,
        @Param("userId") UUID userId);

    /**
     * 사용자별 읽지 않은 알림 개수 조회
     */
    @Query("SELECT COUNT(nt) FROM NotificationTarget nt " +
        "WHERE nt.userId = :userId AND nt.isRead = false")
    Long countUnreadByUserId(@Param("userId") UUID userId);

    /**
     * 사용자별 알림 읽음 처리
     */
    @Modifying
    @Query("UPDATE NotificationTarget nt " +
        "SET nt.isRead = true, nt.readAt = :readAt " +
        "WHERE nt.userId = :userId AND nt.id = :targetId")
    int markAsReadByUserIdAndTargetId(@Param("userId") UUID userId,
        @Param("targetId") UUID targetId,
        @Param("readAt") LocalDateTime readAt);

    /**
     * 사용자의 모든 알림 읽음 처리
     */
    @Modifying
    @Query("UPDATE NotificationTarget nt " +
        "SET nt.isRead = true, nt.readAt = :readAt " +
        "WHERE nt.userId = :userId AND nt.isRead = false")
    int markAllAsReadByUserId(@Param("userId") UUID userId, @Param("readAt") LocalDateTime readAt);

    /**
     * 특정 상태의 알림 대상 조회
     */
    @Query("SELECT nt FROM NotificationTarget nt " +
        "WHERE nt.notificationStatus.id = :statusId " +
        "ORDER BY nt.createdAt DESC")
    List<NotificationTarget> findByStatusId(@Param("statusId") UUID statusId);

    /**
     * 특정 기간 내 알림 대상 조회
     */
    @Query("SELECT nt FROM NotificationTarget nt " +
        "WHERE nt.createdAt BETWEEN :startDate AND :endDate " +
        "ORDER BY nt.createdAt DESC")
    List<NotificationTarget> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    /**
     * 전송되지 않은 알림 대상 조회 (send_at이 null)
     */
    @Query("SELECT nt FROM NotificationTarget nt " +
        "WHERE nt.sendAt IS NULL " +
        "ORDER BY nt.createdAt ASC")
    List<NotificationTarget> findUnsentTargets();

    /**
     * 전송 시간 업데이트
     */
    @Modifying
    @Query("UPDATE NotificationTarget nt " +
        "SET nt.sendAt = :sendAt " +
        "WHERE nt.id = :targetId")
    int updateSendAt(@Param("targetId") UUID targetId, @Param("sendAt") LocalDateTime sendAt);
}