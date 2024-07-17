package com.example.restServer.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.FcmSendDto;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.service.FcmService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {

	@Autowired
	MemberRepository memberRepository;

	private final FcmService fcmService;

	public FcmController(FcmService fcmService) {
		this.fcmService = fcmService;
	}

	@PostMapping("/send")
	public ResponseEntity<Object> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto,
			@RequestHeader(value = "MemberId", required = false) Long memberId) throws IOException {
		System.out.println(fcmSendDto.getToken());
		System.out.println(fcmSendDto.toString());
		log.debug("[+] 푸시 메시지를 전송합니다. ");
		Member member = memberRepository.findById(memberId).get();
		member.setToken(fcmSendDto.getToken());
		memberRepository.save(member);

		int result = fcmService.sendMessageTo(fcmSendDto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}