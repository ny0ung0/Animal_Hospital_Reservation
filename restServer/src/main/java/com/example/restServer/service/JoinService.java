package com.example.restServer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restServer.dto.MemberDto;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.MemberRepository;

@Service
public class JoinService {
	@Autowired
	MemberRepository memberRepository;
	public void JoinUser(MemberDto memberDto) {
		Member member = new Member();
		
		member.setName(memberDto.getName());
		member.setAddress(memberDto.getAddress());
		member.setPhone(memberDto.getPhone());
		member.setNickname(memberDto.getNickname());
		member.setRole(memberDto.getRole());
		memberRepository.save(member);
		Member result = memberRepository.findByNickname(memberDto.getNickname());
		System.out.println(result);
	}

}
