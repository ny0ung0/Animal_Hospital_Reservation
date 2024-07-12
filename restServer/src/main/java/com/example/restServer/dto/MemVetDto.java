package com.example.restServer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemVetDto {
	private Long id;

    private String address;
    private String phone;
    private String businessNumber;
    private String hospitalName;
    private String representative;
    private String businessHours;
    private Boolean partnership;
    private String introduction;
    private String logo;
    
    private String email;
    private boolean bookmarked;
    private Long avgReview;


}
