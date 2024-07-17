package com.example.restServer.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_HM_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    
    
    public static LocalDateTime parseDateTime(String dateTimeStr) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }
    
    public static LocalDateTime parseDateTime(Map<String, String> formData) throws ParseException {
        return DateTimeUtil.parseDateTime(formData.get("date")+" "+formData.get("time"));
    }

    public static LocalTime parseTimeHourMins(String timeStr) throws DateTimeParseException {
        return LocalTime.parse(timeStr, TIME_HM_FORMATTER);
    }
    
    public static LocalTime parseTime (String timeStr) throws DateTimeParseException {
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }
    
    public static Date parseDate(String dateStr) throws ParseException{
        return SIMPLE_DATE_FORMATTER.parse(dateStr);
    }

    public static String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }
    
   
}

