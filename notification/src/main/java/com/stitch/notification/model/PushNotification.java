package com.stitch.notification.model;

import com.stitch.commons.model.entity.BaseEntity;
import lombok.*;

import jakarta.persistence.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "push_notification")
public class PushNotification extends BaseEntity {

    @Column(name = "user_id")
    private String customerId;

    @Column(name = "token", unique = true)
    private String token;
}
