package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/common")
public class NotificationController_nyoung {

	@GetMapping("/notification")
	public String sendNotification() {
		
		return"hospital/notification";
	}
	
	@GetMapping("/notifyframe")
	public String notificationsub() {
		
		return "/notifyframe";
	}
}
