package com.example.restServer.dto;

import java.time.LocalDateTime;

import com.example.restServer.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListDto {

	private Long chatRoomId;
    private String userName;
    private String hospitalName;
    private String lastMessage;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMessageSendDate;
	
}
