package org.ever._4ever_be_alarm.notification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.ever._4ever_be_alarm.notification.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    /**
     * 타입명으로 조회
     */
    @Query("SELECT c FROM Channel c WHERE c.typeName = :typeName")
    Optional<Channel> findByTypeName(@Param("typeName") String typeName);

    /**
     * 타입명 존재 여부 확인
     */
    @Query("SELECT COUNT(c) > 0 FROM Channel c WHERE c.typeName = :typeName")
    boolean existsByTypeName(@Param("typeName") String typeName);

    /**
     * 활성화된 채널 목록 조회 (사용 중인 채널)
     */
    @Query("SELECT DISTINCT c FROM Channel c " +
        "JOIN c.notificationChannels nc " +
        "ORDER BY c.typeName")
    List<Channel> findActiveChannels();
}
