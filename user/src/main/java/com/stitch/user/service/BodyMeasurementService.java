package com.stitch.user.service;

import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;

public interface BodyMeasurementService {
    BodyMeasurementDto createBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress);

    BodyMeasurementDto upDateBodyMeasurement(BodyMeasurementRequest bodyMeasurementRequest, String customerEmailAddress);
}
