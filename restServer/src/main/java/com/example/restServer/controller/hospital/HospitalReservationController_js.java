package com.example.restServer.controller.hospital;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.UnavailableTimeDto;
import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Reservation;
import com.example.restServer.entity.UnavailableTime;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.repository.UnavailableTimeRepository;

import jakarta.servlet.http.HttpServletRequest;

//@CrossOrigin("*")
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
	
	@Autowired
	UnavailableTimeRepository unavailableTimeRepo;
	
	@GetMapping("/reservation/waiting")
	public ResponseEntity<List<Reservation>> getWaitingReservation(HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    Long memberId = Long.parseLong(memberIdHeader);
	    
		System.out.println("대기 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(memberId, "대기");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/confirmed")
	public ResponseEntity<List<Reservation>> getConfirmedReservation(HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    Long memberId = Long.parseLong(memberIdHeader);
	    
		System.out.println("확정 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(memberId, "확정");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/complete")
	public ResponseEntity<List<Reservation>> getCompleteReservation(HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    Long memberId = Long.parseLong(memberIdHeader);
	    
		System.out.println("완료 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(memberId, "완료");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/cancle")
	public ResponseEntity<List<Reservation>> getCancleReservation(HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    Long memberId = Long.parseLong(memberIdHeader);
	    
		System.out.println("취소 예약정보가져오기");
		List<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(memberId, "취소");
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/{id}")
	public ResponseEntity<Reservation> getHospitalReservationById( @PathVariable("id") Long id){
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
	public ResponseEntity<List<Doctor>> getDoctorList(HttpServletRequest request){
		//System.out.println("의사 리스트 가져오기");
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    Long memberId = Long.parseLong(memberIdHeader);
	    //System.out.println("memberId: " + memberId);
	    //System.out.println("Authorization: " + authHeader);

	    // 인증 또는 권한 검사 로직을 여기에 추가할 수 있습니다

	    List<Doctor> list = doctorRepo.findAllByHospitalId(memberId);
	    return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/business-hours")
	public ResponseEntity<String> getHospitalBusinessHours(HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    Long memberId = Long.parseLong(memberIdHeader);
		Member member = memberRepo.findById(memberId).get();
		String businessHours = member.getBusinessHours();
		return new ResponseEntity<>(businessHours, HttpStatus.OK);
	}
	
	
	@GetMapping("/reservation/doctor/{doctorId}")
	public ResponseEntity<List<Reservation>> getDoctorReservation(@PathVariable("doctorId")Long doctorId){

		List<Reservation> list = reservationRepo.findAllByDoctorId(doctorId);
	
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/doctor/unavailable-time/{doctorId}")
	public ResponseEntity<List<UnavailableTime>> getDoctorunavailableTime(@PathVariable("doctorId")Long doctorId){
		System.out.println("doctorId" + doctorId);
		List<UnavailableTime> list = unavailableTimeRepo.findAllByDoctorIdNoReservation(doctorId);
		//System.out.println("여기인거 맞지?");
		//System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PostMapping("/doctor/unavailable-time")
	public ResponseEntity<String> saveUnavailableTime(@RequestBody UnavailableTimeDto unavailableTimeDto){
		System.out.println("unavailableTimeDto date " + unavailableTimeDto.getDate());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = null;
		try {
			date = format.parse(unavailableTimeDto.getDate());
			System.out.println();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Doctor doctor = doctorRepo.findById(unavailableTimeDto.getDoctorId()).get();
		Member member = memberRepo.findById(unavailableTimeDto.getHospitalId()).get();
		System.out.println("date" + date);
		List<String> times =  unavailableTimeDto.getTime();
		for(int i = 0 ; i < times.size(); i++) {
			String timeStr = times.get(i);
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime time = LocalTime.parse(timeStr, timeFormatter);
			System.out.println(time);
			UnavailableTime unavailableTime = new UnavailableTime();
			unavailableTime.setComment(unavailableTimeDto.getComment());
			unavailableTime.setDate(date);
			unavailableTime.setDoctor(doctor);
			unavailableTime.setHospital(member);
			unavailableTime.setTime(time);
			System.out.println(unavailableTime);
			UnavailableTime unavailableTime2 = unavailableTimeRepo.findTimeByDoctorIdNDatetime(unavailableTimeDto.getDoctorId(), unavailableTimeDto.getDate(), times.get(i));
			if(unavailableTime2 != null) {
				unavailableTime.setId(unavailableTime2.getId());
				unavailableTimeRepo.save(unavailableTime);
			}else {
				unavailableTimeRepo.save(unavailableTime);
			} 	
		}
		
		return new ResponseEntity<>("아ㅏㅇ", HttpStatus.OK);
	}
	
	
}
