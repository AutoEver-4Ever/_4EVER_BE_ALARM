package org.ever._4ever_be_alarm.notification.repository;

import java.util.List;
import java.util.Optional;
import org.ever._4ever_be_alarm.common.entity.NotificationErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationErrorCodeRepository extends
    JpaRepository<NotificationErrorCode, Long> {

    /**
     * 에러 코드로 조회
     */
    @Query("SELECT nec FROM NotificationErrorCode nec WHERE nec.errorCode = :errorCode")
    Optional<NotificationErrorCode> findByErrorCode(@Param("errorCode") Integer errorCode);

    /**
     * 에러 메시지로 조회
     */
    @Query("SELECT nec FROM NotificationErrorCode nec WHERE nec.errorMessage = :errorMessage")
    Optional<NotificationErrorCode> findByErrorMessage(@Param("errorMessage") String errorMessage);

    /**
     * 에러 코드 존재 여부 확인
     */
    @Query("SELECT COUNT(nec) > 0 FROM NotificationErrorCode nec WHERE nec.errorCode = :errorCode")
    boolean existsByErrorCode(@Param("errorCode") Integer errorCode);

    /**
     * 에러 메시지 존재 여부 확인
     */
    @Query("SELECT COUNT(nec) > 0 FROM NotificationErrorCode nec WHERE nec.errorMessage = :errorMessage")
    boolean existsByErrorMessage(@Param("errorMessage") String errorMessage);

    /**
     * 활성화된 에러 코드 목록 조회 (사용 중인 에러 코드)
     */
    @Query("SELECT DISTINCT nec FROM NotificationErrorCode nec " +
        "JOIN nec.notificationLogs nl " +
        "ORDER BY nec.errorCode")
    List<NotificationErrorCode> findActiveErrorCodes();
}
