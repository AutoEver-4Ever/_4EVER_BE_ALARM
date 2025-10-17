package org.ever._4ever_be_alarm.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 무엇을 위한 Entity인가?
@Entity
@Table(name = "notification_template")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationTemplate extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "template_name", nullable = false, length = 50)
    private String templateName;

    @Column(name = "title_template", nullable = false, length = 100)
    private String titleTemplate;

    @Column(name = "message_template", nullable = false, length = 100)
    private String messageTemplate;

    @Column(name = "variables", length = 100)
    private String variables;

    @Builder
    public NotificationTemplate(String templateName, String titleTemplate, String messageTemplate,
        String variables) {
        this.templateName = templateName;
        this.titleTemplate = titleTemplate;
        this.messageTemplate = messageTemplate;
        this.variables = variables;
    }
}
