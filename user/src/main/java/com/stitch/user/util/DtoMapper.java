package com.stitch.user.util;

import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;
import com.stitch.user.model.entity.BodyMeasurement;
import org.springframework.beans.BeanUtils;

public class DtoMapper {

    public static BodyMeasurement bodyMeasurementRequestToEntity(BodyMeasurementRequest bodyMeasurementRequest){
        BodyMeasurement bodyMeasurement = new BodyMeasurement();
        BeanUtils.copyProperties(bodyMeasurementRequest, bodyMeasurement);
        return bodyMeasurement;
    }

    public static BodyMeasurementDto bodyMeasurementEntityToDto(BodyMeasurement bodyMeasurement){
        BodyMeasurementDto bodyMeasurementDto = new BodyMeasurementDto();
        BeanUtils.copyProperties(bodyMeasurement, bodyMeasurementDto);
        return bodyMeasurementDto;
    }
}
