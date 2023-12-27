package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VTPassDto<T>{
    private boolean success;
    private T data;
}
