package org.ever._4ever_be_alarm.notification.adapter.web.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ever._4ever_be_alarm.common.validation.AllowedValues;
import org.ever._4ever_be_alarm.common.validation.ValidUuidV7;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.request.NotificationMarkReadRequestDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationCountResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationListResponseDto;
import org.ever._4ever_be_alarm.notification.adapter.web.dto.response.NotificationReadResponseDto;
import org.ever._4ever_be_alarm.notification.domain.port.in.NotificationQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationQueryUseCase notificationQueryUseCase;

    /**
     * 알림 목록 조회
     */
    @GetMapping("/list/{userId}")
    public ResponseEntity<org.ever._4ever_be_alarm.common.response.PageResponseDto<NotificationListResponseDto>> getNotificationList(
        @ValidUuidV7
        @PathVariable("userId")
        UUID userId,
        @AllowedValues(
            allowedValues = {"createdAt"},
            ignoreCase = true,
            message = "sortBy는 createdAt만 허용됩니다."
        )
        @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt")
        String sortBy,
        @AllowedValues(
            allowedValues = {"asc", "desc"},
            ignoreCase = true,
            message = "order는 asc 또는 desc만 허용됩니다."
        )
        @RequestParam(name = "order", required = false, defaultValue = "desc")
        String order,
        @AllowedValues(
            allowedValues = {"PR", "SD", "IM", "FCM", "HRM", "PP", "CUS", "SUP"},
            ignoreCase = true,
            message = "유효하지 않은 source 값입니다."
        )
        @RequestParam(name = "source", required = false)
        String source,
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        @RequestParam(name = "page", required = false, defaultValue = "0")
        Integer page,
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        @Max(value = 100, message = "페이지 크기는 최대 100까지 가능합니다.")
        @RequestParam(name = "size", required = false, defaultValue = "20")
        Integer size
    ) {
        log.info(
            "알림 목록 조회 요청 - userId: {}, sortBy: {}, order: {}, source: {}, page: {}, size: {}",
            userId, sortBy, order, source, page, size
        );

        var result = notificationQueryUseCase.getNotifications(
            userId, sortBy, order, source, page, size
        );

        log.info("알림 목록 조회 성공 - userId: {}, total: {}", userId,
            result.getPage().getTotalElements());
        return ResponseEntity.ok(result);
    }

    /**
     * 알림 갯수 조회
     */
    @GetMapping("/count/{userId}")
    public ResponseEntity<NotificationCountResponseDto> getNotificationCount(
        @ValidUuidV7
        @PathVariable("userId")
        String userId,
        @AllowedValues(
            allowedValues = {"READ", "UNREAD"},
            ignoreCase = true,
            message = "유효하지 않은 status 값입니다. 허용값: READ, UNREAD"
        )
        @RequestParam(name = "status", required = false, defaultValue = "UNREAD") String status
    ) {
        log.info("알림 갯수 조회 요청 - userId: {}, status: {}", userId, status);

        UUID userUuid = UUID.fromString(userId);
        Integer count = notificationQueryUseCase.getUnreadCount(userUuid);

        NotificationCountResponseDto response = NotificationCountResponseDto.builder()
            .count(count)
            .build();

        log.info("알림 갯수 조회 성공 - userId: {}, count: {}", userId, count);
        return ResponseEntity.ok(response);
    }

    /**
     * 알림 읽음 처리 (목록)
     */
    @PatchMapping("/list/read")
    public ResponseEntity<NotificationReadResponseDto> markReadList(
        @Valid
        @RequestBody
        NotificationMarkReadRequestDto request
    ) {
        log.info("알림 읽음 처리 요청 - userId: {}, notificationIds: {}",
            request.getUserId(), request.getNotificationIds());

        Integer processedCount = notificationQueryUseCase.markAsReadList(
            request.getUserId(),
            request.getNotificationIds()
        );

        NotificationReadResponseDto response = NotificationReadResponseDto.builder()
            .processedCount(processedCount)
            .build();

        log.info("알림 읽음 처리 성공 - userId: {}, processedCount: {}",
            request.getUserId(), processedCount);
        return ResponseEntity.ok(response);
    }

    /**
     * 알림 읽음 처리 (전체)
     */
    @PatchMapping("/all/read")
    public ResponseEntity<NotificationReadResponseDto> markReadAll(
        @RequestParam("userId") String userId
    ) {
        log.info("전체 알림 읽음 처리 요청 - userId: {}", userId);

        try {
            UUID userUuid = UUID.fromString(userId);
            Integer processedCount = notificationQueryUseCase.markAsReadAll(userUuid);

            NotificationReadResponseDto response = NotificationReadResponseDto.builder()
                .processedCount(processedCount)
                .build();

            log.info("전체 알림 읽음 처리 성공 - userId: {}, processedCount: {}", userId, processedCount);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 userId: {}", userId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("전체 알림 읽음 처리 실패 - userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 알림 읽음 처리 (단일)
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationReadResponseDto> markReadOne(
        @PathVariable("notificationId") String notificationId,
        @RequestParam("userId") String userId
    ) {
        log.info("단일 알림 읽음 처리 요청 - userId: {}, notificationId: {}", userId, notificationId);

        try {
            UUID userUuid = UUID.fromString(userId);
            boolean success = notificationQueryUseCase.markAsReadOne(userUuid, notificationId);

            NotificationReadResponseDto response = NotificationReadResponseDto.builder()
                .processedCount(success ? 1 : 0)
                .build();

            log.info("단일 알림 읽음 처리 성공 - userId: {}, notificationId: {}, success: {}",
                userId, notificationId, success);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 userId 또는 notificationId", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("단일 알림 읽음 처리 실패 - userId: {}, notificationId: {}", userId, notificationId,
                e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
