package com.stitch.notification.model;


import com.stitch.notification.model.enums.MessageSeverity;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.stitch.commons.model.entity.BaseEntity;

import jakarta.persistence.*;



@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "in_app_notification")
public class InAppNotification extends BaseEntity {

    @Column(name = "notification_id", unique = true, nullable = false)
    private String notificationId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", length = 655)
    private String content;

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private MessageSeverity severity;

    @Column(name = "read")
    private boolean read = false;
}
