package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VTPassCableInfoRequest {
    private String serviceID;
    private String billersCode;
}
