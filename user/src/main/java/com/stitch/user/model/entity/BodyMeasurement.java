package com.stitch.user.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.validation.constraints.*;

@Entity
@Getter
@Setter
@Audited
@Table(name = "body_measurement")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BodyMeasurement extends BaseEntity {

    @Column(name = "tag")
    private String tag;

    @NotNull(message = "Neck length is required")
    @Min(value=1, message="neck length: positive number, min 18 is required")
    @Max(value=100, message="neck length: positive number, max 100 is required")

    @Column(name = "neck")
    private int neck;

    @Column(name = "shoulder")
    private int shoulder;

    @Column(name = "chest")
    private int chest;

    @Column(name = "tummy")
    private int tummy;

    @Column(name = "hip_width")
    private int hipWidth;

    @Column(name = "neck_to_hip_length")
    private int neckToHipLength;

    @Column(name = "short_sleeve_at_biceps")
    private int shortSleeveAtBiceps;

    @Column(name = "mid_sleeve_at_elbow")
    private int midSleeveAtElbow;

    @Column(name = "long_sleeve_at_wrist")
    private int longSleeveAtWrist;

    @Column(name = "waist")
    private int waist;

    @Column(name = "thigh")
    private int thigh;

    @Column(name = "knee")
    private int knee;

    @Column(name = "ankle")
    private int ankle;

    @Column(name = "trouser_length")
    private int trouserLength;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_address", referencedColumnName = "email_address")
    private UserEntity userEntity;

}