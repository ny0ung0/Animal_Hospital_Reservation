package com.example.restServer.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Reservation;
import com.example.restServer.repository.ReservationRepository;

@RestController
@RequestMapping("/api/v1/user")
public class ReviewController_jun {

	@Autowired
	ReservationRepository reservationRepository;
	
	@PostMapping("/review")
	public ResponseEntity<?> reviewInsert(@ModelAttribute Reservation review){
		System.out.println("리뷰 들어옴 : " + review);
		Reservation reservation =reservationRepository.findById(review.getId()).get();
		reservation.setReview(review.getReview());
		reservation.setRating(review.getRating());
		reservationRepository.save(reservation);
		return ResponseEntity.ok().body("성공");
	}
}
