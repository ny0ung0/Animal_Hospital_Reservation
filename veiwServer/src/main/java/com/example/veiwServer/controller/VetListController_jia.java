package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
@Controller
public class VetListController_jia {

	
	@RequestMapping("/vet_list_region")
	public void vetListRegion() {
	}
	@RequestMapping("/vet_detail")
	public void vetDetail(@RequestParam("id") Long id, Model model) {
		model.addAttribute("id", id);
	}
	@RequestMapping("/reserv_form")
	public void reserv_form(@RequestParam("id") Long id, Model model) {
		model.addAttribute("id", id);
	}
	@RequestMapping("/reserv_edit")
	public void reserv_edit(@RequestParam("id") Long id, Model model) {
		model.addAttribute("id", id);
	}
}
