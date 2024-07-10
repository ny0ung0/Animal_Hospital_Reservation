package com.example.restServer.controller.admin;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.IMemberLoginDto;
import com.example.restServer.dto.PointAddDto;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Point;
import com.example.restServer.entity.Support;
import com.example.restServer.repository.LoginRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.repository.SupportRepository;
import com.example.restServer.util.MailService;

import jakarta.annotation.Resource;

@CrossOrigin("*")
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
    public ResponseEntity<List<Support>> getSupportQna(){
        List<Support> list = supportRepo.findAllByCategoryQna();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
	
	@GetMapping("/support/qna/category/{category}/keyword/{keyword}")
    public ResponseEntity<List<Support>> getSupportQnaFindByKeyword(@PathVariable("category")String category, @PathVariable("keyword")String keyword){
        if(category.equals("all")) {
        	List<Support> list = supportRepo.findAllByCategoryQnaByAllCategoryKeyword(keyword);
       	 	return new ResponseEntity<>(list, HttpStatus.OK);
       }else {
    	   List<Support> list = supportRepo.findAllByCategoryQnaByCategoryKeyword(category, keyword);
       	 return new ResponseEntity<>(list, HttpStatus.OK);
       }
    }
	
	@GetMapping("/support/qna/category/{category}")
    public ResponseEntity<List<Support>> getSupportQnaFindByCategory(@PathVariable("category")String category){
        if(category.equals("all")) {
        	 List<Support> list = supportRepo.findAllByCategoryQna();
        	 return new ResponseEntity<>(list, HttpStatus.OK);
        }else {
        	 List<Support> list = supportRepo.findAllByCategoryQnaByCategory(category);
        	 return new ResponseEntity<>(list, HttpStatus.OK);
        }
       
    }
	
}
