package org.ever._4ever_be_alarm.notification.repository;

import java.util.List;
import org.ever._4ever_be_alarm.common.entity.NotificationChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationChannelRepository extends JpaRepository<NotificationChannel, Long> {

    /**
     * 특정 알림의 채널 조회
     */
    @Query("SELECT nc FROM NotificationChannel nc " +
        "WHERE nc.notification.id = :notificationId")
    List<NotificationChannel> findByNotificationId(@Param("notificationId") Long notificationId);

    /**
     * 알림 ID로 채널 삭제
     */
    @Query("DELETE FROM NotificationChannel nc " +
        "WHERE nc.notification.id = :notificationId")
    void deleteByNotificationId(@Param("notificationId") Long notificationId);
}
