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

@Entity
@Table(name = "channel_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelType extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_name", nullable = false, length = 100)
    private String typeName;

    // @OneToMany(mappedBy = "channelType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Notification> notifications = new ArrayList<>();

    @Builder
    public ChannelType(String typeName) {
        this.typeName = typeName;
    }
}
