package com.example.restServer.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.service.user.ReservationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class ReservationController_jia {

	@Autowired
	private ReservationService reservationService;
	
	@GetMapping("/reservation")
	public ResponseEntity<List<Map<String, Object>>> vetList(@RequestParam("id") Long hosId, HttpServletRequest request) {
		List<Map<String, Object>> infoList = new ArrayList<>();
		 
		//보호자 아이디를 주면 => 반려동물 이름이랑 쿠폰여부랑 포인트
		Long userId = Long.parseLong(request.getHeader("username"));
		Long hospitalId = hosId;
		Map<String, Object> userInfo = reservationService.getPetInfo(userId, hospitalId);
		infoList.add(userInfo);
		
		//(의사 목록, 시간목록)
		Map<String, Object> vetAvailInfo =reservationService.getVetAvailInfo(hospitalId);
		infoList.add(vetAvailInfo);
		System.out.println(vetAvailInfo);
		
		//(병원)
		Map<String, Object> vetInfo = reservationService.getVetInfo(hospitalId);
		infoList.add(vetInfo);
		
		return ResponseEntity.ok(infoList);
	}
	
	@PostMapping("/reservation")
	public ResponseEntity<String> makingReservation(@RequestBody Map<String, String> formData, HttpServletRequest request){
		Long userId = Long.parseLong(request.getHeader("username"));
		
		reservationService.makeReservation(formData, userId);
		return ResponseEntity.ok("");
	}
	

}