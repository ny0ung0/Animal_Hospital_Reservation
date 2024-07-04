package com.example.restServer.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.MemberDto;
import com.example.restServer.entity.Member;
import com.example.restServer.service.JoinService;

@RestController
public class LoginController_jun {

	@Autowired
	JoinService joinService;
	

	
	@PostMapping("/joinUser")
	public ResponseEntity<?> joinUser(@RequestBody MemberDto memberDto){
		System.out.println(memberDto);
		joinService.JoinUser(memberDto);
		
		return ResponseEntity.ok().body("성공");
	}
	
}
