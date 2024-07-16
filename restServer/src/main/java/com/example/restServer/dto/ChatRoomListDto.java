package com.example.restServer.dto;

import java.time.LocalDateTime;

import com.example.restServer.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListDto {

    private Long chatId;
    private Member sender;
    private Member receiver;
    private String message;
    private Boolean isRead;
    private LocalDateTime sendDate;
    private Long chatRoomId;
    private Member user;
    private Member hospital;
	
}
