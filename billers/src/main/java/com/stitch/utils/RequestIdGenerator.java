package com.stitch.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RequestIdGenerator {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");


    public static  String generateRequestId() {
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Africa/Lagos"));
        String requestId = dateTime.format(formatter);
        return requestId + RandomStringUtils.randomAlphanumeric(15);
    }
}
