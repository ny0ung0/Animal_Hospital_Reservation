package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EtcController_jun {

	@RequestMapping("/user/fav_vet_list")
	public void fav_vet_list() {
		
	}
	
	@RequestMapping("/user/reserv_list")
	public void reserv_list(@RequestParam(value="btn", required =false)String btn,Model model) {
		model.addAttribute("btn", btn);
	}
	
	@RequestMapping("/user/review_list")
	public void review_list() {
		
	}
}
