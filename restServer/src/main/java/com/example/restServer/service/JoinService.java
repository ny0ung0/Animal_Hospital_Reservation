package com.example.restServer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restServer.dto.JoinHospitalDto;
import com.example.restServer.dto.MemberDto;
import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Login;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.LoginRepository;
import com.example.restServer.repository.MemberRepository;

@Service
public class JoinService {
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
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
		member.setStatus("승인");
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
	
	
	public void joinHospital(JoinHospitalDto joinHospitalDto) {
		Member member = new Member();
		member.setAddress(joinHospitalDto.getAddress());
		member.setPhone(joinHospitalDto.getPhone());
		member.setBusinessNumber(joinHospitalDto.getBusinessNumber());
		member.setHospitalName(joinHospitalDto.getHospitalName());
		member.setRepresentative(joinHospitalDto.getRepresentative());
		member.setBusinessHours(joinHospitalDto.getBusinessHours());
		member.setPartnership(joinHospitalDto.getPartnership());
		member.setIntroduction(joinHospitalDto.getIntroduction());
		member.setRole(joinHospitalDto.getRole());
		member.setEmail(joinHospitalDto.getEmail());
		member.setStatus("대기");
		member = memberRepository.save(member);
		//System.out.println(member);
		Login login = new Login();
		login.setMember(member);
		login.setUsername(joinHospitalDto.getUsername());
		String password = bCryptPasswordEncoder.encode(joinHospitalDto.getPassword());
		login.setPassword(password);
		login.setRole(joinHospitalDto.getRole());
		loginRepository.save(login); 
		
		String doctor_ =joinHospitalDto.getDoctorNames();
		System.out.println("doctor_:"+doctor_);
		String[] doctors = doctor_.split("//");
		
		for(String d : doctors) {
			System.out.println("doctor:"+d);
			Doctor doctor = new Doctor();
			doctor.setHospital(member);
			doctor.setName(d);
			doctorRepository.save(doctor);
		}
 		
		
		
	}

}
