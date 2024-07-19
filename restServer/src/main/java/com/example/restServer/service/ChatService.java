package com.example.restServer.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restServer.dto.ChatRoomListDto;
import com.example.restServer.dto.IChatRoomListDto;
import com.example.restServer.entity.Chat;
import com.example.restServer.entity.ChatRoom;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.ChatRepository;
import com.example.restServer.repository.ChatRoomRepository;
import com.example.restServer.repository.MemberRepository;

@Service
public class ChatService {
	@Autowired
	private ChatRoomRepository chatroomRepo;
	
	@Autowired
	private ChatRepository chatRepo;
	
	@Autowired
	private MemberRepository memberRepo;
	

	//채팅방 리스트 불러오기
	//추후 유저에 따른 채팅방 분리
	public List<ChatRoom> chatList(Long memberId){
		Member member = memberRepo.findById(memberId).get();
		
		List<ChatRoom> chatRoomList = chatroomRepo.findByUser(member);
		
		return chatRoomList;
	}
	
	
//	public List<ChatRoomListDto> getChatRoomsAndLastMessagesByMemberId(Long memberId) {
//        List<Object[]> results = chatRepo.findChatsAndRoomsByMemberId(memberId);
//
//        List<ChatRoomListDto> chatRoomWithLastMessageDtos = new ArrayList<>();
//        for (Object[] result : results) {
//            Long chatRoomId = ((Long) result[0]);
//            Long userId = ((Long) result[1]).longValue();
//            Long hospitalId = ((Long) result[2]).longValue();
//            Long chatId = ((Long) result[3]).longValue();
//            Long senderId = ((Long) result[4]).longValue();
//            Long receiverId = ((Long) result[5]).longValue();
//            String message = (String) result[6];
//            Boolean isRead = (Boolean) result[7];
//            LocalDateTime sendDate = ((Timestamp) result[8]).toLocalDateTime();
//
//            Member user = memberRepo.findById(userId).orElse(null);
//            Member hospital = memberRepo.findById(hospitalId).orElse(null);
//            Member sender = memberRepo.findById(senderId).orElse(null);
//            Member receiver = memberRepo.findById(receiverId).orElse(null);
//
//            ChatRoom chatRoom = new ChatRoom();
//            chatRoom.setId(chatRoomId);
//            chatRoom.setUser(user);
//            chatRoom.setHospital(hospital);
//
//            Chat lastChat = new Chat();
//            lastChat.setId(chatId);
//            lastChat.setSender(sender);
//            lastChat.setReceiver(receiver);
//            lastChat.setMessage(message);
//            lastChat.setIsRead(isRead);
//            lastChat.setChatRoom(chatRoom);
//            lastChat.setSendDate(sendDate);
//
//            ChatRoomListDto dto = new ChatRoomListDto();
//            dto.setChatRoomId(chatRoom.getId());
//            dto.setUserName(user != null ? user.getName() : "Unknown");
//            dto.setHospitalName(hospital != null ? hospital.getName() : "Unknown");
//            dto.setLastMessage(lastChat.getMessage());
//            dto.setLastMessageSendDate(lastChat.getSendDate());
//
//            chatRoomWithLastMessageDtos.add(dto);
//        }
//
//        return chatRoomWithLastMessageDtos;
//    }
//	
	
	public List<ChatRoomListDto> getChatRoomList(Long memberId) {
        //Member member = memberRepo.findById(memberId).get();

        List<IChatRoomListDto> results = chatRepo.findChatsAndRoomsByMemberId(memberId);

        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm");
        
        for (IChatRoomListDto result : results) {
            //ChatRoom chatRoom = (ChatRoom) result[0];
            //Chat lastChat = (Chat) result[1];

            ChatRoomListDto dto = new ChatRoomListDto();
            dto.setChatRoomId(result.getChatRoomId());
            Member user = memberRepo.findById(result.getUser()).get();
            dto.setUserName(user.getName());
            Member hospital = memberRepo.findById(result.getHospital()).get();
            dto.setHospitalName(hospital.getHospitalName());
            dto.setLastMessage(result.getMessage());
            dto.setLastMessageSendDate(result.getSendDate());
            

            chatRoomListDtos.add(dto);
        }
        
        System.out.println("채팅방 목록 데이터 출력 : "+chatRoomListDtos);
        return chatRoomListDtos;
    }
	
	
	
	
	//채팅 내역 불러오기
	public List<Chat> getMessages(Long chatRoomId){
		return chatRepo.findChatsByChatRoomId(chatRoomId);
	}
	
	
	//메시지 전송
	public Chat saveMessage(Long senderId, Long receiverId, String message) {
	    Member sender = memberRepo.findById(senderId).orElseThrow(() -> new IllegalArgumentException("Sender not found with id: " + senderId));
	    Member receiver = memberRepo.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("Receiver not found with id: " + receiverId));

	    // 채팅방 데이터가 있는지 확인하고, 없으면 새로 생성
	    ChatRoom chatRoom = getOrCreateChatRoom(sender, receiver);

	    Chat chatMessage = new Chat();
	    chatMessage.setChatRoom(chatRoom);
	    chatMessage.setSender(sender);
	    chatMessage.setReceiver(receiver);
	    chatMessage.setIsRead(false);
	    chatMessage.setMessage(message);
	    chatMessage.setSendDate(LocalDateTime.now());

	    return chatRepo.save(chatMessage);
	}

	//채팅방 찾기	
	public ChatRoom getChatRoom(Member sender, Member receiver) {
	    return chatroomRepo.findByMembers(sender, receiver);
	}
	
	//채팅방 생성 OR 채팅방 불러오기 
	private ChatRoom getOrCreateChatRoom(Member sender, Member receiver) {

	    ChatRoom chatRoom = chatroomRepo.findByMembers(sender, receiver);
        if (chatRoom != null) {
            return chatRoom;
        } else {
            // 채팅 방이 없으면 새로 생성
            ChatRoom newChatRoom = new ChatRoom();
            newChatRoom.setUser(sender);
            newChatRoom.setHospital(receiver);
            return chatroomRepo.save(newChatRoom);
        }
	}
	
	
	
}
