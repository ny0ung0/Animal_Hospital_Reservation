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
	private ReservationRepository reservRepo;
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
	
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	
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
        String dateTimeStr = formData.get("date") + " " + formData.get("time");
        LocalDateTime dateTime= null;
        
        // 문자열 -> LocalDateTime
        try {
            dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            reservation.setReservationDatetime(dateTime);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
		if(!formData.get("point").equals("") || !formData.get("point").equals("0")) {
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
		reservRepo.save(reservation);
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
	
	public Reservation findReservInfo(Long reservId) {
		System.out.println(reservRepo.findById(reservId).get());
		return reservRepo.findById(reservId).get();
	}
	
	
	public String editReservation(Map<String, String> formData, Long userId) {
		Date now = new Date();
		Long reservId = Long.parseLong(formData.get("reservId"));
	
		Reservation reservation = reservRepo.findById(reservId).get();
		
		//기존 unavailableTime삭제해준뒤, 새로운 unavailableTime등록해주기
        LocalDateTime oriDateTime= reservation.getReservationDatetime();
      	UnavailableTime oriUnavailTime = unavailableTimeRepo.findTimeByDoctorIdNDatetime(reservation.getDoctor().getId(), oriDateTime.format(formatter1), oriDateTime.format(formatter2));
      	unavailableTimeRepo.delete(oriUnavailTime);
      	
      	
		reservation.setUser(reservation.getUser());
		reservation.setHospital(reservation.getHospital());
		reservation.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
		reservation.setStatus("대기");
		reservation.setType(formData.get("type"));
		reservation.setPet(petRepo.findById(Long.parseLong(formData.get("pet"))).get());
		
		if(formData.get("memo") != "") {
			reservation.setMemo(formData.get("memo"));
		}else {
			reservation.setMemo(reservation.getMemo());
		}
				
		// 포맷터
        String dateTimeStr = formData.get("date") + " " + formData.get("time");
        LocalDateTime dateTime= null;
        
        // 문자열 -> LocalDateTime
        try {
            dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            reservation.setReservationDatetime(dateTime);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        
        //수정된 폼에 포인트가 없을 때
        if(formData.get("point").equals("") || formData.get("point").equals("0")) {
        	//원래 예약에 포인트를 사용했었을때
        	if(reservation.getPointsUsed() != null) {
        		//기존 포인트 사용기록만큼 다시 포인트 넣어주기
        		Point newPoint = new Point();
        		newPoint.setUser(reservation.getUser());
        		newPoint.setPointsAccumulated(reservation.getPointsUsed());
        		newPoint.setAccumulationDate(now);
        		newPoint.setComment("예약취소포인트반환");
    			pointRepo.save(newPoint);
        	}
        	reservation.setPointsUsed(null);
        }else{
        	//수정된 폼에 포인트가 있을때
        	if(reservation.getPointsUsed() != null) {
        		//원래 예약에 포인트를 사용했었을때
        		//기존 포인트 사용기록만큼 다시 포인트 넣어주고, 새 포인트 기록 넣어주기
        		Point newPoint = new Point();
        		newPoint.setUser(reservation.getUser());
        		newPoint.setPointsAccumulated(reservation.getPointsUsed());
        		newPoint.setAccumulationDate(now);
        		newPoint.setComment("예약취소포인트반환");
    			pointRepo.save(newPoint);
        	}
        	
    		//원래 예약에 포인트가 없었을때
    		//새포인트 넣어주기
			reservation.setPointsUsed(Integer.parseInt(formData.get("point")));
			Point usedPoint = new Point();
			usedPoint.setUser(reservation.getUser());
			usedPoint.setUseDate(now);
			usedPoint.setPointsUsed(Integer.parseInt(formData.get("point")));
			usedPoint.setComment("예약포인트사용");
			pointRepo.save(usedPoint);
        }
        
        //수정된 폼에 쿠폰이 없을 때
        if(formData.get("coupon").equals("쿠폰사용 안함")) {
        	//원래 예약에 쿠폰을 사용했었을때
        	if(reservation.getCoupon() != null) {
        		Coupon cp = reservation.getCoupon();
        		//기존 쿠폰 사용기록, 사용날짜 false로
        		cp.setIsUsed(false);
        		cp.setUseDate(null);
        		couponRepo.save(cp);
        	}
        	reservation.setCoupon(null);
        }else {
        	//수정된 폼에 쿠폰이 있을때
        	if(reservation.getCoupon() != null) {
        		//원래 예약에 쿠폰을 사용했었을때
        		//기존 쿠폰 사용기록 삭제, 새 쿠폰 기록 넣어주기
        		Coupon cp = reservation.getCoupon();
        		//기존 쿠폰 사용기록, 사용날짜 false로
        		cp.setIsUsed(null);
        		cp.setUseDate(null);
        		couponRepo.save(cp);
        	}
    		//원래 예약에 쿠폰이 없었을때
    		//새 쿠폰기록 넣어주기
        	Coupon coupon = couponRepo.findById(Long.parseLong(formData.get("coupon"))).get();
			coupon.setIsUsed(true);
			coupon.setUseDate(now);
			reservation.setCoupon(coupon);
			couponRepo.save(coupon);
        }
        
        
        //기존 unavailableTime삭제해준뒤, 새로운 unavailableTime등록해주기
		UnavailableTime unavailTime = new UnavailableTime();
		unavailTime.setDoctor(doctorRepo.findById(Long.parseLong(formData.get("doctorId"))).get());
		unavailTime.setHospital(reservation.getHospital());
		unavailTime.setComment("진료예약");
		Date date = java.sql.Timestamp.valueOf(dateTime);
		LocalTime localTime = dateTime.toLocalTime();
		unavailTime.setDate(date);
		unavailTime.setTime(localTime);
		unavailableTimeRepo.save(unavailTime);
        
		reservRepo.save(reservation);
		
		
		return "";
	}
}
