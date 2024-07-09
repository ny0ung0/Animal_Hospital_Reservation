package com.example.veiwServer.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LoginController_jun {

	
	@RequestMapping("/jun")
	public void jun() {
		
	}
	
	@RequestMapping("/register_form")
	public void register_form(Model model) throws StreamReadException, DatabindException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> regionData = objectMapper.readValue(
	            new ClassPathResource("static/json/korea-administrative-district.json").getFile(),
	            new TypeReference<List<Map<String, Object>>>() {
	            });
	      Map<String, List<String>> regions = regionData.stream()
	            .collect(Collectors.toMap(map -> map.keySet().iterator().next(), // key 값 (서울특별시, 부산광역시 등)
	                  map -> (List<String>) map.values().iterator().next() // value 값 (구, 군 리스트)
	            ));
	      model.addAttribute("regions", regions);
	      model.addAttribute("days", Arrays.asList("월", "화", "수", "목", "금", "토", "일", "공휴일"));
	}
	
	@RequestMapping("/login_form")
	public void login_form() {
		
	}
	
	
	
	//user 관련
	@RequestMapping("/user/my_info_edit")
	public void my_info_edit() {
		
	}
}
