package org.ever._4ever_be_alarm.notification.repository;

import java.util.Optional;
import java.util.UUID;
import org.ever._4ever_be_alarm.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, UUID> {

    /**
     * 상태명으로 조회
     */
    @Query("SELECT ns FROM NotificationStatus ns WHERE ns.statusName = :statusName")
    Optional<NotificationStatus> findByStatusName(@Param("statusName") String statusName);

    /**
     * 상태명 존재 여부 확인
     */
    @Query("SELECT COUNT(ns) > 0 FROM NotificationStatus ns WHERE ns.statusName = :statusName")
    boolean existsByStatusName(@Param("statusName") String statusName);
}