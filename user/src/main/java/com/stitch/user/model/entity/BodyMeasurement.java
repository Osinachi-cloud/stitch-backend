package com.stitch.user.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
//import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
//@Audited
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
    private double neck;

    @Column(name = "shoulder")
    private double shoulder;

    @Column(name = "chest")
    private double chest;

    @Column(name = "tummy")
    private double tummy;

    @Column(name = "hip_width")
    private double hipWidth;

    @Column(name = "neck_to_hip_length")
    private double neckToHipLength;

    @Column(name = "short_sleeve_at_biceps")
    private double shortSleeveAtBiceps;

    @Column(name = "mid_sleeve_at_elbow")
    private double midSleeveAtElbow;

    @Column(name = "long_sleeve_at_wrist")
    private double longSleeveAtWrist;

    @Column(name = "waist")
    private double waist;

    @Column(name = "thigh")
    private double thigh;

    @Column(name = "knee")
    private double knee;

    @Column(name = "ankle")
    private double ankle;

    @Column(name = "trouser_length")
    private double trouserLength;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_address", referencedColumnName = "email_address")
    private UserEntity userEntity;
}