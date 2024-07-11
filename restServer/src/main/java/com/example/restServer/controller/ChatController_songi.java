package com.example.restServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.entity.Chat;
import com.example.restServer.entity.ChatRoom;
import com.example.restServer.service.ChatService;

@RestController
public class ChatController_songi {
	
	@Autowired
	private ChatService chatService;
	
		
//	@MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public Chat sendMessage(@Payload Chat chatMessage) {
//        ChatRoom chatRoom = chatService.getChatRoom(chatMessage.getSender(), chatMessage.getReceiver());
//        
//        return chatService.sendMessage(chatRoom, chatMessage.getSender(), chatMessage.getMessage(), chatMessage.getReceiver());
//    }

	
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Chat addUser(@Payload Chat chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        //Add user to the WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender().getName());
        return chatMessage;
    }
	

}
