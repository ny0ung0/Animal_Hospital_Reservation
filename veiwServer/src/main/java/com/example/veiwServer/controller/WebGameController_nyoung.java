package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/webGame")
@Controller
public class WebGameController_nyoung {
	
	@GetMapping("/")
	public String start() {
		return "petGame/web_Pet";
	}
}
