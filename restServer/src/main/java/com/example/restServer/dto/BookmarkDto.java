package com.example.restServer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkDto  {

 
    private Long id;
    private Long hospotal_id;
    private Long user_id;
    
    private String hospital_name;
    private String address;
    private String phone;
    
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    
   
   
    
}
