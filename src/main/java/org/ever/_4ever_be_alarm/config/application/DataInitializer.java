package org.ever._4ever_be_alarm.config.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.Channel;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.NotificationStatus;
import org.ever._4ever_be_alarm.notification.adapter.jpa.entity.Source;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.ChannelRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.NotificationStatusRepository;
import org.ever._4ever_be_alarm.notification.adapter.jpa.repository.SourceRepository;
import org.ever._4ever_be_alarm.notification.domain.model.constants.ChannelNameEnum;
import org.ever._4ever_be_alarm.notification.domain.model.constants.NotificationStatusEnum;
import org.ever._4ever_be_alarm.notification.domain.model.constants.SourceTypeEnum;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final SourceRepository sourceRepository;
    private final ChannelRepository channelRepository;
    private final NotificationStatusRepository notificationStatusRepository;

    @Override
    @Transactional // 모든 초기화 작업을 하나의 트랜잭션으로 묶음
    public void run(ApplicationArguments args) throws Exception {
        log.info("애플리케이션 시작... 기본 데이터 초기화를 시작합니다.");

        initSourceData();
        initChannelData();
        initNotificationStatusData();
        initNotificationErrorCodeData();
        initNotificationTemplateData();

        log.info("기본 데이터 초기화 완료.");
    }

    private void initSourceData() {
        log.info("Source 데이터 확인 중...");
        SourceTypeEnum[] sources = SourceTypeEnum.values();

        for (SourceTypeEnum source : sources) {
            if (!sourceRepository.existsBySourceName(source)) {
                sourceRepository.save(new Source(source));
                log.info("'{}' Source 추가됨.", source);
            }
        }
    }

    private void initChannelData() {
        log.info("Channel 데이터 확인 중...");
        ChannelNameEnum[] channels = ChannelNameEnum.values();

        for (ChannelNameEnum channel : channels) {
            if (!channelRepository.existsByName(channel)) {
                channelRepository.save(new Channel(channel));
                log.info("'{}' Channel 추가됨.", channel);
            }
        }
    }

    private void initNotificationStatusData() {
        log.info("NotificationStatus 데이터 확인 중...");
        NotificationStatusEnum[] statuses = NotificationStatusEnum.values();

        for (NotificationStatusEnum status : statuses) {
            if (!notificationStatusRepository.existsByStatusName(status)) {
                notificationStatusRepository.save(new NotificationStatus(status));
                log.info("'{}' NotificationStatus 추가됨.", status);
            }
        }
    }

    // TODO : Notification_Error_Code, Notification_Template 초기화 구현
    private void initNotificationErrorCodeData() {
        log.info("NotificationErrorCode 데이터 확인 중...");
        // 구현 예정
    }

    private void initNotificationTemplateData() {
        log.info("NotificationTemplate 데이터 확인 중...");
        // 구현 예정
    }
}