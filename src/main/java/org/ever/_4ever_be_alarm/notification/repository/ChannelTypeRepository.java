package org.ever._4ever_be_alarm.notification.repository;

import java.util.List;
import java.util.Optional;
import org.ever._4ever_be_alarm.common.entity.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelTypeRepository extends JpaRepository<ChannelType, Long> {

    /**
     * 타입명으로 조회
     */
    @Query("SELECT ct FROM ChannelType ct WHERE ct.typeName = :typeName")
    Optional<ChannelType> findByTypeName(@Param("typeName") String typeName);

    /**
     * 타입명 존재 여부 확인
     */
    @Query("SELECT COUNT(ct) > 0 FROM ChannelType ct WHERE ct.typeName = :typeName")
    boolean existsByTypeName(@Param("typeName") String typeName);

    /**
     * 활성화된 채널 타입 목록 조회 (사용 중인 타입)
     */
    @Query("SELECT DISTINCT ct FROM ChannelType ct " +
        "JOIN ct.notifications n " +
        "ORDER BY ct.typeName")
    List<ChannelType> findActiveChannelTypes();
}
