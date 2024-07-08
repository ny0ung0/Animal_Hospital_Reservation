package com.example.restServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.MemberDto;
import com.example.restServer.service.JoinService;

@RestController
@RequestMapping("/api/v1")
public class LoginController_jun {

	@Autowired
	JoinService joinService;
	

	
	@PostMapping("/joinUser")
	public ResponseEntity<?> joinUser(@RequestBody MemberDto memberDto){
		System.out.println(memberDto);
		joinService.joinUser(memberDto);
		
		return ResponseEntity.ok().body("성공");
	}
	

	@PostMapping("/aaa")
	public String aaa() {
		System.out.println("aaa컨트롤러 들어옴");
		
		return "aaa 접근성공";
	}
}