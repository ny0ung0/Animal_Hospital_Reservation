package com.example.restServer.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.IMemberEditDto;
import com.example.restServer.dto.MemberEditDto;
import com.example.restServer.service.JoinService;

@RestController
@RequestMapping("/api/v1/user")
public class InfoController_jun {
	
	@Autowired
	JoinService joinService;
	

	@GetMapping("/userInfo/{MemberId}")
	public ResponseEntity<?> getUserInfo(@PathVariable("MemberId") String MemberId){
		System.out.println("겟유저인포 컨트롤러 들어옴");
		List<IMemberEditDto> result =joinService.getEditUserInfo(MemberId);
		System.out.println(result);
		return ResponseEntity.ok().body(result);
	}
	
	@PutMapping("/userInfo")
	public ResponseEntity<?> UpdateUserInfo(@RequestBody MemberEditDto memberEditDto){
		System.out.println(memberEditDto);
		joinService.updateEditUserInfo(memberEditDto);
		return ResponseEntity.ok().body("수정하였습니다.");
	}
}
