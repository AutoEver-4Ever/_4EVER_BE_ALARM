package org.ever._4ever_be_alarm.alarm.repository;

import java.util.List;
import java.util.Optional;
import org.ever._4ever_be_alarm.common.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    /**
     * 타입명으로 조회
     */
    @Query("SELECT nt FROM NotificationType nt WHERE nt.typeName = :typeName")
    Optional<NotificationType> findByTypeName(@Param("typeName") String typeName);

    /**
     * 타입명 존재 여부 확인
     */
    @Query("SELECT COUNT(nt) > 0 FROM NotificationType nt WHERE nt.typeName = :typeName")
    boolean existsByTypeName(@Param("typeName") String typeName);

    /**
     * 활성화된 타입 목록 조회 (사용 중인 타입)
     */
    @Query("SELECT DISTINCT nt FROM NotificationType nt " +
        "JOIN nt.notifications n " +
        "ORDER BY nt.typeName")
    List<NotificationType> findActiveTypes();
}
