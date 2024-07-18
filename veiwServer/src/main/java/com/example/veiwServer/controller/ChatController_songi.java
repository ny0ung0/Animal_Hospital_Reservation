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
	
	
	//새로운 채팅방
	@RequestMapping("/user/newChat/{hosId}")
	public String newChat(@PathVariable("hosId")Long hospitalId, Model model) {
		model.addAttribute("hospitalId",hospitalId);
		
		return "/user/chatroom";
	}
	
	
	//기존에 하던 채팅방
	@RequestMapping("/user/chat/{chatRoomId}")
	public String chat(@PathVariable("chatRoomId")Long chatRoomId, Model model) {
		model.addAttribute("chatRoomId",chatRoomId);
		
		return "/user/chatroom";
	}
	
}
