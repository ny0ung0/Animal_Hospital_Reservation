package com.example.restServer.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Support;
import com.example.restServer.repository.SupportRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/mypage")
public class MyQnaController_jisun {

	@Autowired
	SupportRepository supportRepo;
	
	@GetMapping("/qna-list")
	public ResponseEntity<List<Support>> getMyQnaList(HttpServletRequest request){
		
		String memberIdHeader = request.getHeader("MemberId");
		String authHeader = request.getHeader("Authorization");

		if (memberIdHeader == null || authHeader == null) {
	           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	       }

		 Long memberId = Long.parseLong(memberIdHeader);
		 //Member member = memberRepo.findById(memberId).get();
		   
		 List<Support> list = supportRepo.findAllByMemberIdOrderByCreatedAt(memberId);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
}
