package com.stitch.user.service;

import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;

import java.util.List;

public interface BodyMeasurementService {
    BodyMeasurementDto createBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress);
    BodyMeasurementDto upDateBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress);
    List<BodyMeasurementDto> getBodyMeasurementByUser();
}
