package com.example.restServer.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @Column(length = 1000)
    private String businessHours;
    private Boolean partnership;
    private String introduction;
    private String role;
    private String logo;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String token;
    
    private String status;
    private String email;
    
	public void setToken(String token) {
		this.token = token;
	}

    // Getters and Setters
}

