package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class MyPetController_songi {
	
	@RequestMapping("/myPetForm")
	public String myPetForm() {
		
		return "/user/my_pet_form";
	}
	
	@RequestMapping("/myPetList")
	public String myPetList() {
		
		return "/user/my_pet_list";
	}
	
	
	@RequestMapping("/myPetEdit")
	public String myPetEdit(@RequestParam("id")Long id, Model model) {
		model.addAttribute("id",id);
		
		return "/user/my_pet_edit";
	}
}
