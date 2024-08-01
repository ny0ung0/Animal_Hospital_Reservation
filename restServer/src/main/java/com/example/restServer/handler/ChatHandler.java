package com.example.restServer.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.restServer.entity.Chat;
import com.example.restServer.entity.ChatRoom;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler{
	
	private static List<WebSocketSession> list = new ArrayList<>();

	@Autowired
	private ChatService chatService;
	
	@Autowired
	private MemberRepository memberRepo;
	
	 private final ObjectMapper objectMapper;

    @Autowired
    public ChatHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());
        
        if (jsonNode.has("type") && jsonNode.get("type").asText().equals("join")) {
            Long senderId = jsonNode.get("sender").asLong();
            Long receiverId = jsonNode.get("receiver").asLong();
            
            session.getAttributes().put("sender", senderId);
            session.getAttributes().put("receiver", receiverId);
            
            System.out.println("User joined: sender=" + senderId + ", receiver=" + receiverId);
        } else if (jsonNode.has("sender") && jsonNode.has("receiver") && jsonNode.has("message")) {
            Long senderId = jsonNode.get("sender").asLong();
            Long receiverId = jsonNode.get("receiver").asLong();
            String chatMessage = jsonNode.get("message").asText();

            Chat savedMessage = chatService.saveMessage(senderId, receiverId, chatMessage);

            for (WebSocketSession webSocketSession : list) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(savedMessage)));
                }
            }
        } else {
            System.err.println("Missing required fields in the message payload");
            // 여기에서 예외를 던지거나, 클라이언트에 오류 메시지를 전송할 수 있습니다.
        }

        
//        // senderId와 receiverId를 추출합니다.
//        Long senderId = jsonNode.get("sender").asLong();
//        Long receiverId = jsonNode.get("receiver").asLong();
//        String chatMessage = jsonNode.get("message").asText();
//	    
//	    
//	    // 2. ChatService를 사용하여 메시지를 데이터베이스에 저장하고 저장된 메시지를 반환합니다.
//	    Chat savedMessage = chatService.saveMessage(senderId, receiverId, chatMessage);
//
//	    // 3. 모든 연결된 세션에 저장된 메시지를 전송합니다.
//	    for (WebSocketSession webSocketSession : list) {
//	        if (webSocketSession.isOpen()) {
//	            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(savedMessage)));
//	        }
//	    }
	    
	    
    }
	 
	 
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
        log.info(session + " 클라이언트 접속");
        
        
        Long senderId = (Long) session.getAttributes().get("sender");
        Long receiverId = (Long) session.getAttributes().get("receiver");
        
        Member sender = memberRepo.findById(senderId).get();
        Member receiver = memberRepo.findById(receiverId).get();
	    		
        if (senderId != null && receiverId != null) {
            // 데이터베이스에 채팅방 생성
            ChatRoom chatRoom= chatService.getOrCreateChatRoom(sender, receiver);
            log.info("채팅방 생성됨: sender=" + senderId + ", receiver=" + receiverId);
            Long chatRoomId = chatRoom.getId();
            String responseMessage = "ChatRoom ID: " + chatRoomId;
            session.sendMessage(new TextMessage(responseMessage));
        } else {
            log.error("채팅방 생성 실패: sender 또는 receiver 정보가 없음");
            session.sendMessage(new TextMessage("채팅방 생성 실패: sender 또는 receiver 정보가 없음"));
        }
        
        
    }
	
    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");
        list.remove(session);
    }
}