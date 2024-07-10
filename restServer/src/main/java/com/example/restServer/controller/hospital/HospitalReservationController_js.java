package com.example.restServer.controller.hospital;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Reservation;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.repository.ReservationRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/hospital")
public class HospitalReservationController_js {

	@Autowired
	ReservationRepository reservationRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@Autowired
	PointRepository pointRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	@GetMapping("/reservation/waiting")
	public ResponseEntity<List<Reservation>> getWaitingReservation(){
		System.out.println("대기 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(3L, "대기");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/confirmed")
	public ResponseEntity<List<Reservation>> getConfirmedReservation(){
		System.out.println("확정 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(3L, "확정");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/complete")
	public ResponseEntity<List<Reservation>> getCompleteReservation(){
		System.out.println("완료 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(3L, "완료");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/cancle")
	public ResponseEntity<List<Reservation>> getCancleReservation(){
		System.out.println("취소 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(3L, "취소");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/{id}")
	public ResponseEntity<Reservation> getHospitalReservationById(@PathVariable("id") Long id){
		System.out.println("예약정보 디테일 가져오기");
		Optional<Reservation> result = reservationRepo.findById(id);
		Reservation reservation = result.get();
		System.out.println(reservation);
		return new ResponseEntity<>(reservation, HttpStatus.OK);
	}
	
	@PutMapping("/reservation/{id}/{status}")
	public ResponseEntity<Reservation> updateReservationStatus(@PathVariable("id") Long id, @PathVariable("status")String status){
		System.out.println("예약 상태 업데이트");
		
		Optional<Reservation> result = reservationRepo.findById(id);
		Reservation reservation = result.get();
		reservation.setStatus(status);
		Reservation reservation2 = reservationRepo.save(reservation);
		System.out.println(reservation2);
		if(status.equals("취소")) {
			Point point = new Point();
			point.setUser(reservation.getUser());
			point.setPointsAccumulated(reservation.getPointsUsed());
			point.setComment("예약 취소 포인트 반환");
			point.setAccumulationDate(new Date());
			pointRepo.save(point);
		}
		return new ResponseEntity<>(reservation2, HttpStatus.OK);
	}
	
	@GetMapping("/doctor")
	public ResponseEntity<List<Doctor>> getDoctorList(){
		System.out.println("의사 리스트 가져오기");
		List<Doctor> list =  doctorRepo.findAllByHospitalId(3L);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/business-hours")
	public ResponseEntity<String> getHospitalBusinessHours(){
		Member member = memberRepo.findById(3L).get();
		String businessHours = member.getBusinessHours();
		return new ResponseEntity<>(businessHours, HttpStatus.OK);
	}
	
}
