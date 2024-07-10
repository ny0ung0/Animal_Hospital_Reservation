package com.example.restServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberEditDto {
	private String username;
	private String password;
	private String name;
	private String address;
	private String phone;
	private String nickname;    
	private String email;
	private Long memberId;
    
}

