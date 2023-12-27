package com.stitch.commons.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateExtractDto {

    private int second;
    private int minute;
    private int hour;
    private int dayOfMonth;
    private int month;
    private int dayOfWeek;
}
