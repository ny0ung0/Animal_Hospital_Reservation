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
		// 1. WebSocket에서 수신한 메시지를 Chat 객체로 변환합니다.
	    //Chat chatMessage = objectMapper.readValue(message.getPayload(), Chat.class);
	    
	    // WebSocket에서 수신한 메시지를 JsonNode로 변환합니다.
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());

        // senderId와 receiverId를 추출합니다.
        Long senderId = jsonNode.get("sender").asLong();
        Long receiverId = jsonNode.get("receiver").asLong();
        String chatMessage = jsonNode.get("message").asText();
	    
        
	    //Long receiverId = chatMessage.getReceiver();   
	    //Long senderId = chatMessage.getSender();
        
        Member receiver = memberRepo.findById(receiverId).get();
	    Member sender = memberRepo.findById(senderId).get();
	    
	    
	    // 2. ChatService를 사용하여 메시지를 데이터베이스에 저장하고 저장된 메시지를 반환합니다.
	    Chat savedMessage = chatService.saveMessage(receiver, sender, chatMessage);

	    // 3. 모든 연결된 세션에 저장된 메시지를 전송합니다.
	    for (WebSocketSession webSocketSession : list) {
	        if (webSocketSession.isOpen()) {
	            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(savedMessage)));
	        }
	    }
	    
	    
//        String payload = message.getPayload();
//        log.info("payload : " + payload);
//        //페이로드란 전송되는 데이터를 의미한다.
//        for(WebSocketSession sess: list) {
//            sess.sendMessage(message);
//        }
    }
	 
	 
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
        log.info(session + " 클라이언트 접속");
    }
	
    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");
        list.remove(session);
    }
}