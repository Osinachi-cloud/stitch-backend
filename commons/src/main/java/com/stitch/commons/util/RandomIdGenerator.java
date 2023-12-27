package com.stitch.commons.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RandomIdGenerator {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generateNumberId(int count) {
        return RandomStringUtils.randomNumeric(count);
    }


    public static String generateNumberIdWithDateTime(int count) {
        LocalDate date = LocalDate.now(ZoneId.of("Africa/Lagos"));
        if (count < 12) {
            throw new IllegalArgumentException("Count must be at least 12");
        }
        String prefix = date.format(formatter);
        return prefix + RandomStringUtils.randomNumeric(count - 8);
    }
}
