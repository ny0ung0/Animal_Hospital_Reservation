package com.example.restServer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//@Configuration
public class MailConfig {

	 @Bean
	 public JavaMailSender javaMailSender() {
	    return new JavaMailSenderImpl();
	 }
	 
}
