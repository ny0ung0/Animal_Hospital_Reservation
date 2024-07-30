package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class PointController {

	@RequestMapping("/pointManagement")
	public String pointManagement() {
		return "/user/point_management";
		
	}
}
