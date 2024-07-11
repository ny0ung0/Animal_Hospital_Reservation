package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Chat;
import com.example.restServer.entity.ChatRoom;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	//채팅방 채팅들 불러오기
	public List<Chat> findByChatRoom(ChatRoom chatRoom);
	
}
