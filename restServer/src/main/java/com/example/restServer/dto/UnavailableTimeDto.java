package com.example.restServer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnavailableTimeDto {

	    private Long doctorId;
	    private Long hospitalId;
		private String date;
	    private List<String> time;
	    private String comment;
	
}
