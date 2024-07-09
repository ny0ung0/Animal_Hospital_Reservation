package com.example.restServer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	//단순 문자 메일 보내기
	public void sendSimpleEmail(String toEmail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("[견강할고양] 회원 가입 취소 안내");
		message.setTo(toEmail);
		message.setText("안녕하세요 고객님,\r\n"
				+ "\r\n"
				+ "저희 '견강할고양'에 가입해 주셔서 감사드립니다. 안타깝게도, 회원 가입이 취소되었음을 안내드립니다.\r\n"
				+ "\r\n"
				+ "이 결정에 대해 궁금하신 사항이나 추가 정보가 필요하시면 언제든지 저희에게 연락 주세요. 저희는 항상 회원들의 피드백을 소중히 받아들이며, 더 나은 서비스를 제공하기 위해 노력하고 있습니다.\r\n"
				+ "\r\n"
				+ "감사합니다.\r\n"
				);
		
		javaMailSender.send(message);
	}
	
	//HTML 메일 보내기
	public void sendHTMLEmail() {
		
	}
	
	
}


