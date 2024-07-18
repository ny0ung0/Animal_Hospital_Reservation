package com.example.restServer.controller.admin;


import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.IJoinCountDto;
import com.example.restServer.dto.IMemberLoginDto;
import com.example.restServer.dto.PointAddDto;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Support;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.LoginRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.repository.SupportRepository;
import com.example.restServer.util.MailService;

import jakarta.annotation.Resource;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/manager")
public class AdminController_jisun {
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	LoginRepository loginRepo;
	
	@Autowired
	PointRepository pointRepo;
	
	@Autowired
	SupportRepository supportRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@Resource(name = "mailService")
	private MailService mailService;

	@GetMapping("/permit")
	public ResponseEntity<List<IMemberLoginDto>> getHospitalNonePermit(){
		List<IMemberLoginDto> list = memberRepo.findByStatusWaiting();
		System.out.println(list.toString());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PutMapping("/permit/{id}")
	public ResponseEntity<Member> updatePermitOk(@PathVariable("id") Long id){
		Optional<Member> result = memberRepo.findById(id);
		Member member = result.get();
		member.setStatus("승인");
		memberRepo.save(member);
		if(member.getEmail() != null) {
			mailService.sendHTMLEmail(member.getEmail(), member.getHospitalName());
		}else {
			System.out.println("이메일이 없네용");
		}
		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	
	@DeleteMapping("/permit/{id}")
	public ResponseEntity<String> deletePermitOk(@PathVariable("id") Long id){
		Optional<Member> result = memberRepo.findById(id);
		Member member = result.get();
		if(member.getEmail() != null) {
			mailService.sendSimpleEmail(member.getEmail());
		}else {
			System.out.println("이메일이 없네용");
		}
		
		loginRepo.deleteByMemberId(id);
		doctorRepo.deleteByHospitalId(id);
		memberRepo.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<IMemberLoginDto>> getUserList(){
		List<IMemberLoginDto> list = memberRepo.findAllUserAddUsername();
		System.out.println(list.toString());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/hospital")
	public ResponseEntity<List<IMemberLoginDto>> getHospitalList(){
		List<IMemberLoginDto> list = memberRepo.findAllHospitalAddUsername();
		System.out.println(list.toString());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/hospital/keyword/{keyword}")
	public ResponseEntity<List<IMemberLoginDto>> getHospitalListBykeyword(@PathVariable("keyword")String keyword){
		//System.out.println("여기왔냐");
		List<IMemberLoginDto> list = memberRepo.findAllHospitalAddUsernameByKeyword(keyword);
		System.out.println(list.toString());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/user/keyword/{keyword}")
	public ResponseEntity<List<IMemberLoginDto>> getUserListBykeyword(@PathVariable("keyword")String keyword){
		//System.out.println("여기왔냐22" + keyword);
		List<IMemberLoginDto> list = memberRepo.findAllUserAddUsernameByKeyword(keyword);
		System.out.println(list.toString());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<IMemberLoginDto> getUser(@PathVariable("id")Long id){
		IMemberLoginDto member = memberRepo.findByIdAddUsername(id);
		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	
	
	@GetMapping("/point/{id}")
	public ResponseEntity<List<Point>> getUserPoint(@PathVariable("id")Long id){
		List<Point> pointList = pointRepo.findAllByUserId(id);
		return new ResponseEntity<>(pointList, HttpStatus.OK);
	}
	
	@GetMapping("/point/total/{userId}")
	public ResponseEntity<Integer> getUserPointTotal(@PathVariable("userId")Long userId){
		Integer num = pointRepo.findByUserIdRemainingPoints(userId);
		return new ResponseEntity<>(num, HttpStatus.OK);
	}
	
	@PostMapping("/point")
	public ResponseEntity<String> addPoint(@RequestBody PointAddDto pointAddDto){
		System.out.println("왔냐" + pointAddDto.getUserId());
		Member member = memberRepo.findById(pointAddDto.getUserId()).get();
		Point point = new Point();
		point.setAccumulationDate(new Date());
		point.setComment(pointAddDto.getComment());
		point.setPointsAccumulated(pointAddDto.getPointsAccumulated());
		point.setUser(member);
		pointRepo.save(point);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	@GetMapping("/support/qna")
    public ResponseEntity<Page<Support>> getSupportQna(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "0") int size){
		System.out.println("page test 중");
		//int size = 10;
		Pageable pageable = PageRequest.of(page, size);
		Page<Support> list = supportRepo.findAllByCategoryQna(pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
	
	@GetMapping("/test2")
	public ResponseEntity<Page<Support>> getPosts(@RequestParam(name = "page", defaultValue = "0") int page) {
		System.out.println("test 중");
	    int size = 10;
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Support> supportPage = supportRepo.findAll(pageable);
	    
	    // 전체 데이터 개수
	    long totalElements = supportPage.getTotalElements();

	    // 가져온 데이터와 전체 데이터 개수를 함께 반환
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("X-Total-Elements", String.valueOf(totalElements));
	    
	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(supportPage);
	}

	
	@GetMapping("/support/qna/no-answer")
    public ResponseEntity<List<Support>> getSupportQnaNoAnswer(){
        List<Support> list = supportRepo.findAllByCategoryQnaNoAnswer();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
	
	@GetMapping("/support/qna/category/{category}/keyword/{keyword}")
    public ResponseEntity<Page<Support>> getSupportQnaFindByKeyword(@RequestParam(name = "page", defaultValue = "0") int page, @PathVariable("category")String category, @PathVariable("keyword")String keyword){
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
		if(category.equals("all")) {
        	Page<Support> list = supportRepo.findAllByCategoryQnaByAllCategoryKeyword(pageable, keyword);
       	 	return new ResponseEntity<>(list, HttpStatus.OK);
       }else {
    	   Page<Support> list = supportRepo.findAllByCategoryQnaByCategoryKeyword(pageable, category, keyword);
       	 return new ResponseEntity<>(list, HttpStatus.OK);
       }
    }
	
	@GetMapping("/support/qna/category/{category}")
    public ResponseEntity<Page<Support>> getSupportQnaFindByCategory(@RequestParam(name = "page", defaultValue = "0") int page, @PathVariable("category")String category){
       int size = 10;
       Pageable pageable = PageRequest.of(page, size);
		if(category.equals("all")) {
        	 Page<Support> list = supportRepo.findAllByCategoryQna(pageable);
        	 return new ResponseEntity<>(list, HttpStatus.OK);
        }else {
        	 Page<Support> list = supportRepo.findAllByCategoryQnaByCategory(pageable, category);
        	 return new ResponseEntity<>(list, HttpStatus.OK);
        } 
    }
	
	@GetMapping("/member/count")
	public ResponseEntity<List<Long>> getMemberCount(){
		List<Long> list = new ArrayList<>();
		Long userCnt = memberRepo.findAllCountMemberType("ROLE_USER");
		Long hospitalCnt = memberRepo.findAllCountMemberType("ROLE_HOSPITAL");
		list.add(userCnt);
		list.add(hospitalCnt);
		//System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/member/user/join-count")
	public ResponseEntity<List<IJoinCountDto>> getUserCount(){
		List<IJoinCountDto> list= memberRepo.userCountJoinWeek();
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/member/hospital/join-count")
	public ResponseEntity<List<IJoinCountDto>> gethospitalCount(){
		List<IJoinCountDto> list= memberRepo.hospitalCountJoinWeek();
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PutMapping("/support/qna/reply")
	public ResponseEntity<String> updateReply(@RequestBody Support support){
		Support support2 = supportRepo.findById(support.getId()).get();
		support2.setReply(support.getReply());
		support2.setDate(new Date());
		support2.setIsConfirmed(true);
		supportRepo.save(support2);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
}
