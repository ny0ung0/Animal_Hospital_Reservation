package com.example.restServer.dto;

import java.time.LocalDateTime;

public interface IMemberLoginDto {

	Long getid();
    String getName();
    String getAddress();
    String getPhone();
    String getNickname();
    String getBusinessNumber();
    String getHospitalName();
    String getRepresentative();
    String getBusinessHours();
    Boolean getPartnership();
    String getIntroduction();
    String getRole();
    String getLogo();
    String getToken();
    String getStatus();
    String getEmail();
    String getUsername();
    Long getLoginId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
	
}
