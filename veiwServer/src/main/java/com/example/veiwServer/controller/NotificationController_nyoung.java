package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hospital")
public class NotificationController_nyoung {

	@GetMapping("/notification")
	public String sendNotification() {
		
		return"hospital/notification";
	}
}
