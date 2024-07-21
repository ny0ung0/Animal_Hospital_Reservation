package com.example.restServer.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    public static String formatTime1(LocalDateTime time) {
        return time.format(TIME_FORMATTER);
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }
    
    // JSON 문자열을 Map으로 변환하는 메서드
    public static Map<String, List<String>> jsonToMap(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, HashMap.class);
    }

    // Map을 배열로 변환하는 메서드
    public static Map<String, String[]> mapToArray(Map<String, List<String>> businessHoursMap) {
        Map<String, String[]> businessHoursArray = new HashMap<>();
        String[] days = {"mon", "tue", "wed", "thu", "fri", "sat", "sun", "hol"};

        for (String day : days) {
            List<String> hours = businessHoursMap.get(day);
            String[] times = new String[4];

            if (hours != null && !hours.isEmpty()) {
                String openCloseTime = hours.get(0).substring(6).trim();
                if (!openCloseTime.equals("영업 안함")) {
                    String[] openClose = openCloseTime.split("//");
                    times[0] = openClose[0].trim();
                    times[1] = openClose[1].trim();
                    if (hours.size() > 1 && !hours.get(1).substring(6).trim().equals("점심시간 없음")) {
                        String lunchTime = hours.get(1).substring(6).trim();
                        String[] lunch = lunchTime.split("//");
                        times[2] = lunch[0].trim();
                        times[3] = lunch[1].trim();
                    } else {
                        times[2] = "0";
                        times[3] = "0";
                    }
                } else {
                    times[0] = "0";
                    times[1] = "0";
                    times[2] = "0";
                    times[3] = "0";
                }
            } else {
                times[0] = "0";
                times[1] = "0";
                times[2] = "0";
                times[3] = "0";
            }
            businessHoursArray.put(day, times);
        }
        return businessHoursArray;
    
    }
    
    public static String getBusinessHours(String jsonString){
    	  try {
              Map<String, List<String>> businessHoursMap = jsonToMap(jsonString);
              Map<String, String[]> businessHoursArray = mapToArray(businessHoursMap);
              ObjectMapper objectMapper = new ObjectMapper();
              return objectMapper.writeValueAsString(businessHoursArray);
          } catch (IOException e) {
              e.printStackTrace();
          }
    	  return null;
    }
    
   
   
}

