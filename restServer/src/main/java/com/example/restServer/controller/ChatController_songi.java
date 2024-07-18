package com.example.restServer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.ChatRoomListDto;
import com.example.restServer.entity.Chat;
import com.example.restServer.entity.ChatRoom;
import com.example.restServer.service.ChatService;

import jakarta.servlet.http.HttpServletRequest;

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
    
    
    //채팅방 리스트 불러오기
    @GetMapping("/chatList")
    public ResponseEntity<?> chatList(HttpServletRequest request){
       String memberIdHeader = request.getHeader("MemberId");
 	   String authHeader = request.getHeader("Authorization");

 	   if (memberIdHeader == null || authHeader == null) {
     	   String errorMessage = "MemberId 또는 Authorization 헤더가 없습니다.";
     	    return ResponseEntity.badRequest().body(errorMessage);
        }
 	   Long memberId = Long.parseLong(memberIdHeader);
 	   
 	   
 	   List<ChatRoomListDto> chatRoomList = chatService.getChatRoomList(memberId);
    	
 	   System.out.println("채팅방목록 출력 : " + chatRoomList);
 	   
 	   return ResponseEntity.ok(chatRoomList);
 	   
    }

    //채팅 내역 불러오기
    @GetMapping("/chat/{chatRoomId}")
    public ResponseEntity<List<Chat>> getChatMessages(@PathVariable("chatRoomId") Long chatRoomId) {
        List<Chat> chats = chatService.getMessages(chatRoomId);
        System.out.println("채팅내역 출력 : " + chats);
        return ResponseEntity.ok(chats);
    }
    
    

}
