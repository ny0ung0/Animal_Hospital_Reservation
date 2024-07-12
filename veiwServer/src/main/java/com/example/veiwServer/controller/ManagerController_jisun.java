package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager")
public class ManagerController_jisun {
	
	@RequestMapping("/vetPetmit")
	public String vetPermit() {
		return "/manager/vet_permit";
	}
	
	@RequestMapping("/userList")
	public String userList() {
		return "/manager/user_list";
	}
	
	@RequestMapping("/userDetail")
	public String userDetail(@RequestParam("id")Long id, Model model) {
		model.addAttribute("id", id);
		return "/manager/user_detail";
	}
	
	@RequestMapping("/vetDetail")
	public String vetDetail(@RequestParam("id")Long id, Model model) {
		model.addAttribute("id", id);
		return "/manager/vet_detail";
	}
	
	@RequestMapping("/vetList")
	public String vetList() {
		return "/manager/vet_list";
	}
	
	@RequestMapping("/qnaList")
	public String qnaList() {
		return "/manager/qna_list";
	}
	
	@RequestMapping("/")
	public String dashboard() {
		return "/manager/dashboard";
	}
	
	
}
