package com.example.restServer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restServer.dto.MemberDto;
import com.example.restServer.entity.Login;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.LoginRepository;
import com.example.restServer.repository.MemberRepository;

@Service
public class JoinService {
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public void joinUser(MemberDto memberDto) {
		Member member = new Member();
		
		member.setName(memberDto.getName());
		member.setAddress(memberDto.getAddress());
		member.setPhone(memberDto.getPhone());
		member.setNickname(memberDto.getNickname());
		member.setRole(memberDto.getRole());
		member.setEmail(memberDto.getEmail());
		if(loginRepository.existsByUsername(memberDto.getUsername())) {
			return;
		}
		memberRepository.save(member);
		
		Member result = memberRepository.findByNickname(memberDto.getNickname());
		System.out.println(result);
		Login login = new Login();
		
		login.setMember(result);
		login.setUsername(memberDto.getUsername());
		String password = bCryptPasswordEncoder.encode(memberDto.getPassword());
		login.setPassword(password);
		login.setRole(memberDto.getRole());
		//login.setIsApproved(true);
		
		loginRepository.save(login); 
	
		
		
	}

}
