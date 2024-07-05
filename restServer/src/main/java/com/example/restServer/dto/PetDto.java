package com.example.restServer.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDto {
    private String name;
    private String type;
    private String photo;
    private Date birthdate;
    private String gender;
    private Boolean isNeutered;
    private Double weight;
    private String healthIssues;
}
