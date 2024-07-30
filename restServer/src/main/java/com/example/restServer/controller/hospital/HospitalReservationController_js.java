package com.example.restServer.controller.hospital;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.UnavailableTimeDto;
import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Pet;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Reservation;
import com.example.restServer.entity.UnavailableTime;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PetRepository;
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
	
	@Autowired
	PetRepository petRepo;
	
	@GetMapping("/reservation/status/{status}")
	public ResponseEntity<Page<Reservation>> getWaitingReservation(@RequestParam(name = "page", defaultValue = "0") int page, HttpServletRequest request, @PathVariable("status")String status){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    int size = 6;
	    Pageable pageable = PageRequest.of(page, size);
	    Long memberId = Long.parseLong(memberIdHeader);
	    
	    if(status.equals("waiting")) {
	    	status = "대기";
	    }else if(status.equals("confirmed")){
	    	status = "확정";
	    }else if(status.equals("complete")) {
	    	status = "완료";
	    }else if(status.equals("cancle")) {
	    	status = "취소";
	    }
	    
		System.out.println(status + "예약정보가져오기" + memberId);
		Page<Reservation> list = reservationRepo.findAllByHospitalIdAndStatus(pageable, memberId, status);
		long totalElements = list.getTotalElements();
		System.out.println(status + "리스트" + list);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.add("X-Total-Elements", String.valueOf(totalElements));
	    
	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(list);
	}
	

	@GetMapping("/reservation/hospital")
	public ResponseEntity<List<Reservation>> getHospitalReservationById(HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	   
	    Long memberId = Long.parseLong(memberIdHeader);
		System.out.println("병원별 예약 정보가져오기");
		List<Reservation> list = reservationRepo.findByHospitalId(memberId);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	
	@GetMapping("/reservation/{reservId}")
	public ResponseEntity<Reservation> getHospitalReservationById( @PathVariable("reservId") Long reservId){
		System.out.println("예약정보 디테일 가져오기");
		Optional<Reservation> result = reservationRepo.findById(reservId);
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
		
		if(status.equals("취소") && reservation2.getPointsUsed() !=null) {
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
		unavailableTimeRepo.deleteAllByIdDate(unavailableTimeDto.getDoctorId(), date);
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
			unavailableTimeRepo.save(unavailableTime);
		}
		
		return new ResponseEntity<>("Ok", HttpStatus.OK);
	}
	
	@GetMapping("/customer/{filter}")
	public ResponseEntity<Page<Reservation>> getCustomerList(@PathVariable("filter")String filter, @RequestParam(name = "page", defaultValue = "0") int page, HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    int size = 10;
	    Pageable pageable = PageRequest.of(page, size);
	    Long memberId = Long.parseLong(memberIdHeader);
	    System.out.println(filter);
	    if(filter.equals("last-date")) {
	    	Page<Reservation> list = reservationRepo.findByCustomerListLastDate(pageable, memberId);
			return new ResponseEntity<>(list, HttpStatus.OK);
	    }else if(filter.equals("user-name")) {
	    	Page<Reservation> list = reservationRepo.findByCustomerListFilterByUserName(pageable, memberId);
			return new ResponseEntity<>(list, HttpStatus.OK);
	    }else {
	    	Page<Reservation> list = reservationRepo.findByCustomerListFilterByName(pageable, memberId);
			return new ResponseEntity<>(list, HttpStatus.OK);
	    }
	    
	}
	
	@GetMapping("/customer/{filter}/{keyword}")
	public ResponseEntity<Page<Reservation>> getCustomerListKeyword(@PathVariable("filter")String filter, @PathVariable("keyword")String keyword, @RequestParam(name = "page", defaultValue = "0") int page, HttpServletRequest request){
		String memberIdHeader = request.getHeader("memberId");
	    String authHeader = request.getHeader("Authorization");

	    if (memberIdHeader == null || authHeader == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    int size = 10;
	    Pageable pageable = PageRequest.of(page, size);
	    Long memberId = Long.parseLong(memberIdHeader);
	    System.out.println(filter);
	    if(filter.equals("last-date")) {
	    	Page<Reservation> list = reservationRepo.findByCustomerListLastDateKeyword(pageable, memberId, keyword);
			return new ResponseEntity<>(list, HttpStatus.OK);
	    }else if(filter.equals("user-name")) {
	    	Page<Reservation> list = reservationRepo.findByCustomerListFilterByUserNameKeyword(pageable, memberId, keyword);
			return new ResponseEntity<>(list, HttpStatus.OK);
	    }else {
	    	Page<Reservation> list = reservationRepo.findByCustomerListFilterByNameKeyword(pageable, memberId, keyword);
			return new ResponseEntity<>(list, HttpStatus.OK);
	    }
	    
	}
	
	@GetMapping("/pet/{petId}")
	public ResponseEntity<Pet> getPetDetail(@PathVariable("petId")Long petId){
		Pet pet = petRepo.findById(petId).get();
		return new ResponseEntity<>(pet, HttpStatus.OK);
	}
	
	@GetMapping("/reservation/pet/{petId}")
	public ResponseEntity<Page<Reservation>> getPetReservationList(@RequestParam(name = "page", defaultValue = "0") int page, HttpServletRequest request, @PathVariable("petId")Long petId){
		System.out.println("동물별 예약 정보 가져오기");
		
		String memberIdHeader = request.getHeader("memberId");
		String authHeader = request.getHeader("Authorization");
		System.out.println("동물별 예약 정보 가져오기" + memberIdHeader + authHeader);
		if(memberIdHeader == null || authHeader == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		int size = 6;
		Pageable pageable = PageRequest.of(page, size);
		Long hospitalId = Long.parseLong(memberIdHeader);
		
		Page<Reservation> list = reservationRepo.findByPetAndHospitalId(pageable, petId, hospitalId);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
}
