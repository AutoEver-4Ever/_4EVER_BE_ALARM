package org.ever._4ever_be_alarm.notification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.ever._4ever_be_alarm.notification.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, UUID> {

    /**
     * 소스명으로 조회
     */
    @Query("SELECT s FROM Source s WHERE s.sourceName = :sourceName")
    Optional<Source> findBySourceName(@Param("sourceName") String sourceName);

    /**
     * 소스명 존재 여부 확인
     */
    @Query("SELECT COUNT(s) > 0 FROM Source s WHERE s.sourceName = :sourceName")
    boolean existsBySourceName(@Param("sourceName") String sourceName);

    /**
     * 활성화된 소스 목록 조회 (사용 중인 소스)
     */
    @Query("SELECT DISTINCT s FROM Source s " +
        "JOIN s.notifications n " +
        "ORDER BY s.sourceName")
    List<Source> findActiveSources();
}
