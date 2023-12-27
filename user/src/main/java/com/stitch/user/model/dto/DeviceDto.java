package com.stitch.user.model.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private String deviceId;
    private String name;
    private String model;
    private String os;
}
