package com.example.restServer.dto;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.restServer.entity.Doctor;

@Repository
public interface IMemberEditDto {
	 String getUsername();
	 String getPassword();
     String getName();
     String getAddress();
     String getPhone();
     String getNickname();    
     String getEmail();
    
     
     
 	
    
    
    
     
     String getBusinessNumber();
     String getHospitalName();
     String getRepresentative();
     String getBusinessHours();
     Boolean getPartnership();
     String getIntroduction();
     String getLogo();
    
     
     
    
}

