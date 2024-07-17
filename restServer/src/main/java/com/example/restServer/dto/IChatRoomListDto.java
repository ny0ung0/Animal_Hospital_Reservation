package com.example.restServer.dto;

import java.time.LocalDateTime;

public interface IChatRoomListDto {
	Long getChatRoomId();
    Long getUser();
    Long getHospital();
    String getMessage();
    LocalDateTime getSendDate();
}
