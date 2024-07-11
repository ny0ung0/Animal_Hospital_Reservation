package com.example.restServer.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberEditDto {
	//private String username;
	//private String password;
	private String name;
	//private String address;
	//private String phone;
	private String nickname;    
	//private String email;
	private Long memberId;
	
	//private Long id;
	//로그인 테이블
	private String username;
	private String password;
	private String passwordOrigin;
	private String passwordNew;
	private String passwordCheckBox;
	//의사 테이블
    private String doctorNamesField;
    //멤버 테이블
    private String address;
    private String phone;
    private String email;
    private String hospitalName;
    private String representative;
    private String businessHours;
    private Boolean partnership;
    private String introduction;
   
    private MultipartFile file;
    private String logo;
   
    
    private String token;
    private String status;
    private String role;
    private String businessNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public String getFileName() {
		return file.getOriginalFilename();
	}
    
}

