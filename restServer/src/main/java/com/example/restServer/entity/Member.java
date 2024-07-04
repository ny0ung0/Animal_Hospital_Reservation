package com.example.restServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String nickname;
    private String businessNumber;
    private String hospitalName;
    private String representative;
    private String businessHours;
    private Boolean partnership;
    private String introduction;
    private String role;
    private String logo;
    private Boolean isActive;
    private Boolean isBlacklisted;
    private String token;

    // Getters and Setters
}

