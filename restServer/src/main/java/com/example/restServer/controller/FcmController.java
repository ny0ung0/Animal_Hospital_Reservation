package com.example.restServer.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.FcmSendDto;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Notification;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.NotificationRepository;
import com.example.restServer.service.FcmService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	NotificationRepository notificationRepository;

	private final FcmService fcmService;

	public FcmController(FcmService fcmService) {
		this.fcmService = fcmService;
	}

	@PostMapping("/send")
	public ResponseEntity<Object> saveToken(@RequestBody @Validated FcmSendDto fcmSendDto,
			@RequestHeader(value = "MemberId", required = false) Long memberId) throws IOException {
		System.out.println(fcmSendDto.getToken());
		System.out.println(fcmSendDto.toString());
		log.debug("[+] fcm 토큰 저장 ");
		Member member = memberRepository.findById(memberId).get();
		member.setToken(fcmSendDto.getToken());
		memberRepository.save(member);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/message")
	public ResponseEntity<Object> pushMessagetoMe(@RequestBody @Validated FcmSendDto fcmSendDto,
			@RequestHeader(value = "MemberId", required = false) Long memberId) throws IOException {
		
		log.debug("[+] 메세지 전송 ");
		Member receiver = memberRepository.findById(memberId).get();
		Member sender = memberRepository.findById(13L).get();
		
		Notification notification = Notification.builder().sender(sender).receiver(receiver).title(fcmSendDto.getTitle())
				.content(fcmSendDto.getBody()).isRead(false).build();

		notificationRepository.save(notification);


		int result = fcmService.sendMessageTo(fcmSendDto);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/message/{receiverId}")
	public ResponseEntity<Object> pushMessagetoReceiver(@RequestBody @Validated FcmSendDto fcmSendDto,
			@RequestHeader(value = "MemberId", required = false) Long memberId,
			@PathVariable(value = "receiverId") Long receiverId) throws IOException {
		log.debug("[+] 메세지 전송 ");

		Member sender = memberRepository.findById(memberId).get();
		Member receiver = memberRepository.findById(receiverId).get();
		Notification notification = Notification.builder().sender(sender).receiver(receiver).title(fcmSendDto.getTitle())
				.content(fcmSendDto.getBody()).isRead(false).build();

		notificationRepository.save(notification);
		System.out.println("receiver" + receiver.getToken());
		fcmSendDto.setToken(receiver.getToken());
		int result = fcmService.sendMessageTo(fcmSendDto);
		System.out.println(fcmSendDto.toString());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}