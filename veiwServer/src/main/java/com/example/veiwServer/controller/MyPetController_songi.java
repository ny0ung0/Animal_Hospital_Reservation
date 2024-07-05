package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class MyPetController_songi {
	
	@RequestMapping("/myPetForm")
	public String myPetForm() {
		
		return "/user/my_pet_form";
	}
}
