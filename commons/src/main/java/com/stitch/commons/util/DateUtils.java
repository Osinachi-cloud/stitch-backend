package com.stitch.commons.util;

import com.stitch.commons.model.dto.DateExtractDto;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateUtils {

    public static String receiptTimeFormatter(Instant instant){
        LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.of("Africa/Lagos"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/uuuu hh:mm a", Locale.ENGLISH);
        String formatted = time.format(dtf);
        String[] split = formatted.split(" ");
        String date = split[0].replaceAll("/", " ");
        String formattedTime = split[1].concat(" ") + split[2];
        return String.format("%s at %s", date, formattedTime);
    }

    public static DateExtractDto extractDateInfo(Instant instant) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Africa/Lagos"));
//        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        return DateExtractDto.builder()
            .second(zonedDateTime.getSecond())
            .minute(zonedDateTime.getMinute())
            .hour(zonedDateTime.getHour())
            .dayOfMonth(zonedDateTime.getDayOfMonth())
            .month(zonedDateTime.getMonthValue())
            .dayOfWeek(zonedDateTime.getDayOfWeek().getValue())
            .build();
    }


    public static String generateCronExpression(Instant instant, String frequency) {
        ZoneId lagosZone = ZoneId.of("Africa/Lagos");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant.atZone(lagosZone).toInstant(), lagosZone);

        switch (frequency.toUpperCase()) {
            case "ONE_TIME":
                return onceCronExpression(localDateTime);
            case "DAILY":
                return dailyCronExpression(localDateTime);
            case "WEEKLY":
                return weeklyCronExpression(localDateTime);
            case "MONTHLY":
                return monthlyCronExpression(localDateTime);
            default:
                throw new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }

    private static String onceCronExpression(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

    private static String dailyCronExpression(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH * * ? *", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

    private static String weeklyCronExpression(LocalDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        int dayOfWeekValue = dayOfWeek.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH ? * " + dayOfWeekValue + " *", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

    private static String monthlyCronExpression(LocalDateTime dateTime) {
        int dayOfMonth = dateTime.getDayOfMonth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH " + dayOfMonth + " * ? *", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
}
