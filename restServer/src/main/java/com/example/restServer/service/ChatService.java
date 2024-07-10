package com.example.restServer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.restServer.entity.Chat;
import com.example.restServer.entity.ChatRoom;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.ChatRepository;
import com.example.restServer.repository.ChatRoomRepository;

@Service
public class ChatService {
	@Autowired
	private ChatRoomRepository chatroomRepo;
	
	@Autowired
	private ChatRepository chatRepo;

	//채팅방 리스트 불러오기
	//추후 유저에 따른 채팅방 분리
	//@GetMapping("/chatList")
	public List<ChatRoom> chatList(){
		List<ChatRoom> chatRoomList = chatroomRepo.findAll();
		
		return chatRoomList;
	}
	
	//채팅방 생성
	//@PostMapping("/chatroom")
	public ChatRoom createChatRoom(Member user, Member hospital){
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setUser(user);
		chatRoom.setHospital(hospital);
		chatroomRepo.save(chatRoom);
		
		return chatRoom;
	}
	
	
	
	//채팅 내역 불러오기
	public List<Chat> getMessages(ChatRoom chatRoom){
		List<Chat> chatMessages = chatRepo.findByChatRoom(chatRoom);
		return chatMessages;
	}
	
	
	//메시지 전송
	public Chat sendMessage(ChatRoom chatRoom, Member sender, String message) {
        Chat chatMessage = new Chat();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setMessage(message);

        return chatRepo.save(chatMessage);
    }
	
	
	public ChatRoom getChatRoom(Member user, Member hospital) {
        return chatroomRepo.findByUserAndHospital(user, hospital);
    }
	
}
