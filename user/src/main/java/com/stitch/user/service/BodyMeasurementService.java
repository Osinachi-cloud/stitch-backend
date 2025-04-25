package com.stitch.user.service;

import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;

import java.util.List;

public interface BodyMeasurementService {
    BodyMeasurementDto createBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest);
    BodyMeasurementDto updateBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest);
    List<BodyMeasurementDto> getBodyMeasurementByUser();
}
