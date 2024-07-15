package com.example.restServer.service.user;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.UnavailableTime;

@Component
public class ConvertingAop {
	
   public Map<String, String> convertUnavailToString(Date date, LocalTime time) {
       Map<String, String> timeMap = new HashMap<>();
       
       timeMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
       timeMap.put("time", time.toString());

       return timeMap;
    }
   
   public Map<Doctor, List<Map<String, String>>> convertUnavailListToString (List<UnavailableTime> unavailList) {
	   List<Map<String, String>> timeList = new ArrayList<>();
	   Map<Doctor, List<Map<String, String>>> unavailMap = new HashMap<>();
	   unavailList.forEach(ua ->{
			timeList.add(this.convertUnavailToString(ua.getDate(),ua.getTime()));
			unavailMap.put(ua.getDoctor(), timeList);
		});
	   
	   
	   return unavailMap;
	   
   }
}
