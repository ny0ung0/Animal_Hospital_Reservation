package com.example.restServer.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class ReservationListController_jun {

	@GetMapping("/reservationList/{memberId}")
	public ResponseEntity<?> reservationList(@PathVariable("memberId")Long memeberId){
		
		return ResponseEntity.ok().body("");
	}
}
