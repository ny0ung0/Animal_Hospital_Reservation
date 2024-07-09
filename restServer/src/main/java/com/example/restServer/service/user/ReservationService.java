package com.example.restServer.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restServer.entity.Coupon;
import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Pet;
import com.example.restServer.entity.UnavailableTime;
import com.example.restServer.repository.CouponRepository;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PetRepository;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.repository.UnavailableTimeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	//보호자 아이디를 주면 => 반려동물 이름이랑 쿠폰여부랑 포인트
	public Map<String, Object> getPetInfo(Long userId, Long hospitalId) {
		//반려동물 리스트 가져오기
		List<Pet> petList = petRepo.findAllByMemberId(userId);
		List<Coupon> couponList = couponRepo.findCouponByUserAndHospital(userId,hospitalId);
		Map<String, Object> map = new HashMap<>();
		map.put("petList", petList);
		map.put("couponList", couponList);
		return map;
	}
	
	//(의사 목록, 시간목록)
	public Map<String, Object> getVetAvailInfo(Long hospitalId) {
		Map<String, Object> map = new HashMap<>();
		List<Doctor> docList = doctorRepo.findAllByHospitalId(hospitalId);
		System.out.println(docList);
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
}
