package com.stitch.model.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Builder
@Slf4j
public class DataPlanDto implements Serializable {

    private static final long serialVersionUID = 2356L;
    private boolean success;
    private DataPlanContent content;
}
