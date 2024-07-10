package com.example.restServer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginDto {

	private Long id;
    private String name;
    private String address;
    private String phone;
    private String nickname;
    private String businessNumber;
    private String hospitalName;
    private String representative;
    private String businessHours;
    private Boolean partnership;
    private String introduction;
    private String role;
    private String logo;
    private String token;
    private String status;
    private String email;
    private String username;
    private String password;
    private Long loginId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
    
}
