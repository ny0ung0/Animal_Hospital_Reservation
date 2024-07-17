package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.dto.BookmarkDto;
import com.example.restServer.entity.Chat;
import com.example.restServer.entity.ChatRoom;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Pet;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	public ChatRoom findByUserAndHospital(Member user, Member hospital);
	
	public List<ChatRoom> findByUser(Member user);
    
	public List<ChatRoom> findByHospital(Member hospital);
	
	@Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :chatRoomId ORDER BY c.sendDate DESC")
    List<Chat> findChatsByChatRoomId(@Param("chatRoomId") Long chatRoomId);
	
}
