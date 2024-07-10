package com.example.restServer.dto;

import org.springframework.stereotype.Repository;

@Repository
public interface IMemberEditDto {
	 String getUsername();
	 String getPassword();
     String getName();
     String getAddress();
     String getPhone();
     String getNickname();    
     String getEmail();
    
}

