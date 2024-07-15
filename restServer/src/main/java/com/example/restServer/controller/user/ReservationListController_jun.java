package com.example.restServer.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.UserReservationDto;
import com.example.restServer.repository.ReservationRepository;

@RestController
@RequestMapping("/api/v1/user")
public class ReservationListController_jun {

	@Autowired
	private ReservationRepository reservationRepository;
	
	@GetMapping("/reservationList/{memberId}")
	public ResponseEntity<?> reservationList(@PathVariable("memberId")Long memeberId){
		List<UserReservationDto> list =reservationRepository.findReserListByUserId(memeberId);
		for(UserReservationDto dto : list) {
			System.out.println(dto.getPet_name());
		}
		return ResponseEntity.ok().body(list);
	}
}
