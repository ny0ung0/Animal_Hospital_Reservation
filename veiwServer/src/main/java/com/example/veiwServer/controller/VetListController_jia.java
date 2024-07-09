package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
@Controller
public class VetListController_jia {

	
	@RequestMapping("/vet_list_region")
	public void vetListRegion() {
	}
	@RequestMapping("/reserv_form")
	public void reserv_form(@RequestParam("id") Long id, Model model) {
		model.addAttribute("id", id);
		System.out.println(id);
	}
}
