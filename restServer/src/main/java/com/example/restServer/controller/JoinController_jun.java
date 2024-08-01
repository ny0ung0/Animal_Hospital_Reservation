package com.example.restServer.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.JoinHospitalDto;
import com.example.restServer.dto.MemberDto;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.service.JoinService;
import com.example.restServer.service.ValidationService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/common")
public class JoinController_jun {

	@Autowired
	JoinService joinService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	ValidationService validationService;

	
	@PostMapping("/joinUser")
	public ResponseEntity<?> joinUser(@RequestBody MemberDto memberDto){
		System.out.println(memberDto);
		joinService.joinUser(memberDto);
		
		return ResponseEntity.ok().body("성공");
	}
	@PostMapping("/joinHospital")
	public String joinHospital(@ModelAttribute JoinHospitalDto joinHospitalDto){
		System.out.println("병원회원가입 들어옴");
		System.out.println(joinHospitalDto); 
		joinService.joinHospital(joinHospitalDto);
		return "";
	}
 	

	@PostMapping("/aaa")
	public String aaa() {
		System.out.println("aaa컨트롤러 들어옴");
		
		return "aaa 접근성공";
	}
	@GetMapping("/address2/{address1}")
	public List<String> address2(@PathVariable("address1") String address1) throws StreamReadException, DatabindException, IOException
			 {
		//System.out.println("address1에 접근 성공 +" + address1);
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> address2 = null;
		List<Map<String, Object>> regionData = objectMapper.readValue(
				new ClassPathResource("static/json/korea-administrative-district.json").getFile(),
				new TypeReference<List<Map<String, Object>>>() {
				});
		Map<String, List<String>> regions = regionData.stream()
				.collect(Collectors.toMap(map -> map.keySet().iterator().next(), // key 값 (서울특별시, 부산광역시 등)
						map -> (List<String>) map.values().iterator().next() // value 값 (구, 군 리스트)
				));
		for (Map<String, Object> map : regionData) {
			if (map.containsKey(address1)) {
				address2 = (List<String>) map.get(address1);
			}
		}
		//System.out.println("address2 :" + address2);
		return address2;
	}
	@GetMapping("/address1")
	public Map<String, List<String>> address1() throws StreamReadException, DatabindException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> regionData = objectMapper.readValue(
				new ClassPathResource("static/json/korea-administrative-district.json").getFile(),
				new TypeReference<List<Map<String, Object>>>() {
				});
		Map<String, List<String>> regions = regionData.stream()
				.collect(Collectors.toMap(map -> map.keySet().iterator().next(), // key 값 (서울특별시, 부산광역시 등)
						map -> (List<String>) map.values().iterator().next() // value 값 (구, 군 리스트)
				));

		return regions;
	}
	
	@PostMapping("/check/username")
	public String checkUsername(@RequestParam("username")String username) {
		//System.out.println("checkUsername 들어옴");
		//System.out.println(username);
		boolean result = validationService.checkUsername(username);
		
		if(result) {
			return "중복된 아이디입니다. 다른 아이디를 입력해주세요.";
		}
		return "사용가능한 아이디입니다.";
	}
	@PostMapping("/check/nickname")
	public String checkNickname(@RequestParam("nickname")String nickname) {
		//System.out.println("checkNickname 들어옴");
		//System.out.println(nickname);
		boolean result =validationService.checkNickname(nickname);
		System.out.println(result);
		
		if(result) {
			return "중복된 닉네임입니다. 다른 닉네임을 입력해주세요.";
		}
		return "사용가능한 닉네임입니다.";
	}
	
	@PostMapping("/check/nickname/{MemberId}")
	public String checkNicknameEdit(@PathVariable("MemberId") String memberId,  @RequestParam("nickname")String nickname) {
		//System.out.println("checkNickname 들어옴");
		//System.out.println(nickname);
		boolean result =validationService.checkNicknameEdit(nickname, memberId);
		System.out.println(result);
		
		if(result) {
			return "중복된 닉네임입니다. 다른 닉네임을 입력해주세요.";
		}
		return "사용가능한 닉네임입니다.";
	}
	
	
	@PostMapping("/deletetoken/{MemberId}")
	public String deleteToken(@PathVariable("MemberId") Long memberId) {
		
		Optional<Member> memberTK = memberRepository.findById(memberId);
		
		Member member = memberTK.get();
		member.setToken(null);
		
		memberRepository.save(member);
		
		return "DB토큰 삭제 완료";
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}