package com.example.restServer.dto;

import lombok.Data;


public interface BookmarkDto  {

 
   Long getId();
     Long getHospotal_id();
     Long getUser_id();
    
     String getHospital_name();
     String getAddress();
     String getPhone();
    
    //private LocalDateTime created_at;
    //private LocalDateTime updated_at;
    
   
   
    
}
