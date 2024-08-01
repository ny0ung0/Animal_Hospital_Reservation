package com.example.restServer.controller.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.restServer.entity.Reservation;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.service.user.ReservationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class ReservationController_jia {

	@Autowired
	private ReservationService reservationService;
	
	@Autowired 
	private ReservationRepository reservRepo;
	

	@GetMapping("/reservation")
	public List<Map<String, Object>> vetList(@RequestParam("id") Long hosId, HttpServletRequest request) {
		List<Map<String, Object>> infoList = new ArrayList<>();
		 
		//보호자 아이디를 주면 => 반려동물 이름이랑 쿠폰여부랑 포인트
		Long userId = Long.parseLong(request.getHeader("MemberId"));
		Long hospitalId = hosId;
		Map<String, Object> userInfo = reservationService.getPetInfo(userId, hospitalId);
		infoList.add(userInfo);
		
		//(의사 목록, 시간목록)
		Map<String, Object> vetAvailInfo =reservationService.getVetAvailInfo(hospitalId);
		infoList.add(vetAvailInfo);
		
		//(병원)
		Map<String, Object> vetInfo = reservationService.getVetInfo(hospitalId);
		infoList.add(vetInfo);
		
		return infoList;
	}
	
	@PostMapping("/reservation")
	public ResponseEntity<String> makingReservation(@RequestBody Map<String, String> formData, HttpServletRequest request) throws ParseException{
		try {
			Long userId = Long.parseLong(request.getHeader("MemberId"));
			String result = reservationService.makeReservation(formData, userId);
			if(result.equals("success")) {
				return ResponseEntity.ok(result);
			}
		}catch(ResponseStatusException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getReason());		
		}
		 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
	}
	
	@GetMapping("/reservation/{id}")
	public ResponseEntity<List<Map<String, Object>>> reservationInfo(@PathVariable("id") Long reservId, HttpServletRequest request) {
		
		Long userId = Long.parseLong(request.getHeader("MemberId"));
		List<Map<String, Object>> basicList = this.vetList(reservRepo.findById(reservId).get().getHospital().getId(), request);
		Map<String, Object> map = new HashMap<>();
		
		Reservation reserv = reservationService.findReservInfo(reservId);
		map.put("reservation", reserv);
		
		basicList.add(map);
		
		return ResponseEntity.ok(basicList);
	}
	
	@PutMapping("/reservation")
	public ResponseEntity<String> editReservation(@RequestBody Map<String, String> formData, HttpServletRequest request) throws ParseException{
		try {
		Long userId = Long.parseLong(request.getHeader("MemberId"));
		String result = reservationService.editReservation(formData, userId);
			if(result.equals("success")) {
				return ResponseEntity.ok(result);
			}
		}catch(ResponseStatusException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getReason());		
		}
		 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
		}
}
