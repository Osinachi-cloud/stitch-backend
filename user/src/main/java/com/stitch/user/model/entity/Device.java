package com.stitch.user.model.entity;

import com.stitch.commons.model.entity.BaseEntity;
import com.stitch.user.model.dto.DeviceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Table(name = "device")
public class Device extends BaseEntity {

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "os")
    private String os;

    public Device(DeviceDto deviceDto){
        this.name = deviceDto.getName();
        this.model = deviceDto.getModel();
        this.os = deviceDto.getOs();
    }
}
