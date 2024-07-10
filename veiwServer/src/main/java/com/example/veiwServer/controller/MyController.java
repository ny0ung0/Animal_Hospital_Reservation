package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {

	@RequestMapping("/")
	public String userRoot() {
		System.out.println("user index..");
		return "/user/index";
	}
	
	@RequestMapping("/hospital")
	public String hospitalRoot() {
		System.out.println("hospital index..");
		return "/hospital/index";
	}
	
	@RequestMapping("/manager")
	public String adminRoot() {
		System.out.println("manager index..");
		return "/manager/index";
	}
	
	
	@RequestMapping("/qnaForm")
	public String qnaForm() {
		return "qna_form";
	}
	
	
	@RequestMapping("/qnaDetail")
	public String qnaDetail() {
		return "qna_detail";
	}
	
	
	@RequestMapping("/noticeForm")
	public String noticeForm() {
		return "notice_form";
	}
	
	
	@RequestMapping("/noticeList")
	public String noticeList() {
		return "notice_list";
	}
	
	
	@RequestMapping("/noticeDetail/{id}")
	public String noticeDetail(@PathVariable("id")Long id, Model model) {
		model.addAttribute("id",id);
		return "notice_detail";
	}
	
}
