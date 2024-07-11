package com.example.veiwServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatController_songi {
	
	@RequestMapping("/user/chatList")
	public String chatList_user() {
		return "/user/chat_list";
	}
	
	
	@RequestMapping("/user/chat/{id}")
	public String chat(@PathVariable("id")Long id, Model model) {
		model.addAttribute("id",id);
		
		return "/user/chatroom";
	}
	
}
