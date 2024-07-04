package com.example.restServer.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Member;
import com.example.restServer.repository.MemberRepository;

@RestController
public class LoginController_jun {

	@Autowired
	MemberRepository mr;
	
	@GetMapping("/test")
	public Member test() {
		System.out.println("test들어옴");
		Optional<Member> member =mr.findById(1L);
		 System.out.println( member.get());
		return member.get();
	}
	
}
