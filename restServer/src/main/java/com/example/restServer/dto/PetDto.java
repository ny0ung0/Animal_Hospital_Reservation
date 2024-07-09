package com.example.restServer.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDto {
    private String name;
    private String type;
    private String bigtype;
    private MultipartFile photo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private String gender;
    private Boolean isNeutered;
    private Double weight;
    private String healthIssues;
    
    
    
    public String getFileName() {
    	return photo.getOriginalFilename();
    }
    
    
    
}
