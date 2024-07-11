package com.example.restServer.service;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.example.restServer.dto.FCMNotificationRequestDto;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.MemberRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {

	private final FirebaseMessaging firebaseMessaging;
	private final MemberRepository memberRepository;
	
	public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {
	Optional<Member> user = memberRepository.findById(requestDto.getTargetUserId());
	
	if(user.isPresent()) {
		if(user.get().getToken() != null) {
			Notification notification = Notification.builder()
					.setTitle(requestDto.getTitle())
					.setBody(requestDto.getBody())
					.build();
			
			Message message = Message.builder()
					.setToken(user.get().getToken())
					.setNotification(notification)
					.build();
			try {
				firebaseMessaging.send(message);
				return "알람 전송 성공. targetUserId=" + requestDto.getTargetUserId();
			} catch (FirebaseMessagingException e) {
				e.printStackTrace();
				return "알람 전송 실패. targetUserId=" + requestDto.getTargetUserId();
			}
		}else {
			return "서버에 저장된 해당 유저 FirebaseToken이 존재하지 않습니다. targetUserID=" + requestDto.getTargetUserId();
		}
	} else {
		return "해당 유저가 존재하지 않습니다. targetUSerId=" + requestDto.getTargetUserId();
	}
	}
}
