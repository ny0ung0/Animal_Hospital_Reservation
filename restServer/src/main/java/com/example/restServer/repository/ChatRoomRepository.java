package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.ChatRoom;
import com.example.restServer.entity.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	public ChatRoom findByUserAndHospital(Member user, Member hospital);
	
	
}
