package com.example.restServer.dto;

import java.time.LocalDateTime;

public interface UserResevationDto  {

	//reservation
	Long getId();
    String getMemo();
    Integer getPoints_used();
    Integer getRating();
    LocalDateTime getReservation_datetime();
    String getReeview();
    String getStatus();
    String getType();
    String getCoupon_id();
    String getDoctor_id();
    String getHospital_id();
    String getPet_id();
    String getUser_id();
    
    //member(hospital)
    String getHospital_name();
    
    //pet
    String getPet_name();
    String getPet_type();
    String getPhoto();
    String getGender();
    LocalDateTime getBirthdate();
    
    //member(user)
    String getNickname();
    String getPhone();
    
    //coupon
    String getCoupon_name();
    
    //doctor
    String getDoctor_name();
    
    
    
    
    //private LocalDateTime created_at;
    //private LocalDateTime updated_at;
    
   
   
    
}
