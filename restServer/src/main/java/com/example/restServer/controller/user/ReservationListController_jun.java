package com.example.restServer.controller.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.UserReservationDto;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Reservation;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.service.user.ReservationService;
import com.example.restServer.util.DateTimeUtil;

@RestController
@RequestMapping("/api/v1/user")
public class ReservationListController_jun {

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private PointRepository pointRepository;
	
	@Autowired
	private ReservationService reservService;
	
	@GetMapping("/reservationList/{memberId}")
	public ResponseEntity<?> reservationList(@PathVariable("memberId")Long memeberId){
		List<UserReservationDto> list =reservationRepository.findReserListByUserId(memeberId);
		for(UserReservationDto dto : list) {
			System.out.println(dto.getPet_name());
		}
		return ResponseEntity.ok().body(list);
	}
	
	@PutMapping("/reservation/cancel/{reservation_id}")
	public ResponseEntity<?> reservationCancel(@PathVariable("reservation_id") Long reservation_id){
		Reservation reservation =reservationRepository.findById(reservation_id).get();
		reservation.setStatus("취소");
		System.out.println(reservation);
		if(reservation.getPointsUsed() != null ) {
			//System.out.println("포인트 반환 if 들어옴");
			Point point = new Point();
			point.setUser(reservation.getUser());
			point.setPointsAccumulated(reservation.getPointsUsed());
			point.setComment("예약 취소 포인트 반환");
			point.setAccumulationDate(new Date());
			pointRepository.save(point);
		}
		
		
		reservationRepository.save(reservation);
		
		//코커런트 해시맵에서 락객체 삭제
		String slotKey = reservService.getSlotKey(Long.toString(reservation.getDoctor().getId()), DateTimeUtil.formatDate(reservation.getReservationDatetime()), DateTimeUtil.formatTime1(reservation.getReservationDatetime()));
		
		reservService.slotLocks.forEach((key, lockInfo) -> {
			System.out.println(key.equals(slotKey));
            if (key.equals(slotKey)) {
            	reservService.slotLocks.computeIfPresent(key, (k, v) -> null);
            }
        });
		
		return ResponseEntity.ok().body("");
	}
}
