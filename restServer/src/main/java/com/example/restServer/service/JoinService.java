package com.example.restServer.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restServer.dto.IMemberEditDto;
import com.example.restServer.dto.JoinHospitalDto;
import com.example.restServer.dto.MemberDto;
import com.example.restServer.dto.MemberEditDto;
import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Login;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.DoctorRepository;
import com.example.restServer.repository.LoginRepository;
import com.example.restServer.repository.MemberRepository;

@Service
public class JoinService {
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;
	
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
		
		if(joinHospitalDto.getFile() != null && !joinHospitalDto.getFile().isEmpty() ) {
			String originName = joinHospitalDto.getFileName();
			String newName = UUID.randomUUID().toString() + originName;
			member.setLogo(newName);
			File file = new File(newName);
			
			try {
				joinHospitalDto.getFile().transferTo(file);
				System.out.println("파일 업로드 성공....");
				
				//썸네일 생성
				//String thumbnailSaveName = "s_" + newName;
				//board.setThumbnailName(thumbnailSaveName);
				
				//File thumbfile = new File(uploadPath + thumbnailSaveName);
				File ufile = new File(uploadPath + newName);
				
				//Thumbnails.of(ufile).size(100,100).toFile(thumbfile);
				System.out.println(joinHospitalDto);
				
				
			}catch(Exception e){
				
			}
		}
		
		member = memberRepository.save(member);
		
		//System.out.println(member);
		Login login = new Login();
		login.setMember(member);
		login.setUsername(joinHospitalDto.getUsername());
		String password = bCryptPasswordEncoder.encode(joinHospitalDto.getPassword());
		login.setPassword(password);
		login.setRole(joinHospitalDto.getRole());
		loginRepository.save(login); 
		
		String doctor_ =joinHospitalDto.getDoctorNamesField();
		System.out.println("doctor_:"+doctor_);
		String[] doctors = doctor_.split("//");
		if(!(doctors[0].equals(""))){
			for(String d : doctors) {
				System.out.println("doctor:"+d);
				Doctor doctor = new Doctor();
				doctor.setHospital(member);
				doctor.setName(d);
				doctorRepository.save(doctor);
			}
		}
	}
	
	//유저정보 가져오기
	public List<IMemberEditDto> getEditUserInfo(String memberId) {
		List<IMemberEditDto> data = new ArrayList<>();
		data =loginRepository.findByMemberIdEditUser(memberId);
		
		return data;
	}
	
	//유저정보수정
	public void updateEditUserInfo(MemberEditDto memberEditDto) {
		
		if(memberEditDto.getPassword().length()<16) {
			Login login = loginRepository.findByUsername(memberEditDto.getUsername());
			String password = bCryptPasswordEncoder.encode(memberEditDto.getPassword());
			login.setPassword(password);
			loginRepository.save(login);
		}
		Member member = memberRepository.findById(memberEditDto.getMemberId()).get();
		member.setName(memberEditDto.getName());
		member.setEmail(memberEditDto.getEmail());
		member.setAddress(memberEditDto.getAddress());
		member.setPhone(memberEditDto.getPhone());
		member.setNickname(memberEditDto.getNickname());
		memberRepository.save(member);
		
	}
	
	//병원정보 가져오기
	public List<IMemberEditDto> getEditHospitalInfo(String memberId) {
		List<IMemberEditDto> data = new ArrayList<>();
		data = loginRepository.findByMemberIdEditHospital(memberId);
		
		return data;
	}
	

}
