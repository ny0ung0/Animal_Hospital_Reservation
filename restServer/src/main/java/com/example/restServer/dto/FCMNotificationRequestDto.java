package com.example.restServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FCMNotificationRequestDto {
	private Long targetUserId;
	private String title;
	private String body;
	

}
