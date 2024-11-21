package com.stitch.model.dto;

import com.stitch.user.model.dto.BodyMeasurementDto;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductVariationDto {
    private String  color;
    private String bodyMeasurementTag;
    private String bodyMeasurementId;
    private String sleeveType;
}