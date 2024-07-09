package com.example.restServer.service.user;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restServer.entity.Coupon;
import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Pet;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Reservation;
import com.example.restServer.entity.UnavailableTime;
import com.example.restServer.repository.CouponRepository;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PetRepository;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.repository.UnavailableTimeRepository;

@Service
public class ReservationService {

	@Autowired 
	private MemberRepository memRepo;
	@Autowired 
	private ReservationRepository reserveRepo;
	@Autowired 
	private PetRepository petRepo;
	@Autowired 
	private CouponRepository couponRepo;
	@Autowired 
	private DoctorRepository doctorRepo;
	@Autowired 
	private UnavailableTimeRepository unavailableTimeRepo;
	@Autowired 
	private PointRepository pointRepo;
	
	//보호자 아이디를 주면 => 반려동물 이름이랑 쿠폰여부랑 포인트
	public Map<String, Object> getPetInfo(Long userId, Long hospitalId) {
		//반려동물 리스트 가져오기
		List<Pet> petList = petRepo.findAllByMemberId(userId);
		List<Coupon> couponList = couponRepo.findCouponByUserAndHospital(userId,hospitalId);
		Integer point = pointRepo.findByUserIdRemainingPoints(userId);
		List<Integer> pointList = new ArrayList<>();
		pointList.add(point);
		Map<String, Object> map = new HashMap<>();
		map.put("petList", petList);
		map.put("couponList", couponList);
		map.put("pointList", pointList);
		return map;
	}
	
	//(의사 목록, 시간목록)
	public Map<String, Object> getVetAvailInfo(Long hospitalId) {
		Map<String, Object> map = new HashMap<>();
		List<Doctor> docList = doctorRepo.findAllByHospitalId(hospitalId);
		for(int i = 0; i<docList.size(); i++) {
			Long docId = docList.get(i).getId();
			String docName = docList.get(i).getName();
			List<UnavailableTime> unavailList = unavailableTimeRepo.findAllByDoctorId(docId);
			
			map.put(docId+"//"+ docName, unavailList);
		}
		return map;
	}
	
	//(병원)
	public Map<String, Object> getVetInfo(Long hospitalId) {
		Map<String, Object> map = new HashMap<>();
		Member vet = memRepo.findById(hospitalId).get();
		map.put(vet.getHospitalName()+"", vet );
		return map;
	}
	
	public String makeReservation(Map<String, String> formData, Long userId) {
		Date now = new Date();
		System.out.println(formData);
		Reservation reservation = new Reservation();
		reservation.setUser(memRepo.findById(userId).get());
		reservation.setHospital(memRepo.findById(Long.parseLong(formData.get("hospitalId"))).get());
		reservation.setMemo(formData.get("memo"));
		reservation.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
		reservation.setStatus("대기");
		reservation.setType(formData.get("type"));
		reservation.setPet(petRepo.findById(Long.parseLong(formData.get("pet"))).get());
		
		// 포맷터
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateTimeStr = formData.get("date") + " " + formData.get("time");
        LocalDateTime dateTime= null;
        // 문자열 -> LocalDateTime
        try {
            dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            reservation.setReservationDatetime(dateTime);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
		if(formData.get("point") != "") {
			reservation.setPointsUsed(Integer.parseInt(formData.get("point")));
			Point usedPoint = new Point();
			usedPoint.setUser(memRepo.findById(userId).get());
			usedPoint.setUseDate(now);
			usedPoint.setPointsUsed(Integer.parseInt(formData.get("point")));
			usedPoint.setComment("예약포인트사용");
			pointRepo.save(usedPoint);
		}
		if(!formData.get("coupon").equals("쿠폰사용 안함")) {
			Coupon coupon = couponRepo.findById(Long.parseLong(formData.get("coupon"))).get();
			coupon.setIsUsed(true);
			coupon.setUseDate(now);
			reservation.setCoupon(coupon);
			couponRepo.save(coupon);
		}
		reserveRepo.save(reservation);
		UnavailableTime unavailTime = new UnavailableTime();
		unavailTime.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
		unavailTime.setHospital(memRepo.findById(Long.parseLong(formData.get("hospitalId"))).get());
		unavailTime.setComment("진료예약");
		
		Date date = java.sql.Timestamp.valueOf(dateTime);
		LocalTime localTime = dateTime.toLocalTime();
		unavailTime.setDate(date);
		unavailTime.setTime(localTime);
		unavailableTimeRepo.save(unavailTime);
		
		
		return "";
	}
}
