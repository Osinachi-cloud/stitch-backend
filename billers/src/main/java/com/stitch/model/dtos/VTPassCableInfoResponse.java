package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VTPassCableInfoResponse {
    private String name;
    private String number;
}
