package com.example.restServer.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.IMemberLoginDto;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.LoginRepository;
import com.example.restServer.repository.MemberRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/manager")
public class AdminController_jisun {
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	LoginRepository loginRepo;

	@GetMapping("/permit")
	public ResponseEntity<List<IMemberLoginDto>> getHospitalNonePermit(){
		List<IMemberLoginDto> list = memberRepo.findByStatusNull();
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
		loginRepo.deleteByMemberId(id);
		memberRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
}
