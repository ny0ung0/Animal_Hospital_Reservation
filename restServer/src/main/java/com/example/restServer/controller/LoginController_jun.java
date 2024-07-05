package com.example.restServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.MemberDto;
import com.example.restServer.entity.Login;
import com.example.restServer.service.JoinService;

@RestController
public class LoginController_jun {

	@Autowired
	JoinService joinService;
	

	
	@PostMapping("/joinUser")
	public ResponseEntity<?> joinUser(@RequestBody MemberDto memberDto){
		System.out.println(memberDto);
		joinService.joinUser(memberDto);
		
		return ResponseEntity.ok().body("성공");
	}
	
	@PostMapping("/loginUser")
	public ResponseEntity<?> joinUser(@RequestBody Login login){
		System.out.println("로그인 컨트롤러 접속 성공");
		
		return ResponseEntity.ok().body("성공");
	}
	
}
