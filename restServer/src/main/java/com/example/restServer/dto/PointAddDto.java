package com.example.restServer.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointAddDto {

	    private Long userId;
	    private Integer pointsAccumulated;
	    private Date accumulationDate;
	    private String comment;
	    
}
