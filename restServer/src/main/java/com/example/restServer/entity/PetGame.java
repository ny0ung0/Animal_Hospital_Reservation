package com.example.restServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PetGame extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    private Integer feedCount;
    private Integer playCount;
    private Integer maxExperience;
    private Integer currentExperience;
    private Integer level;
    private Boolean isFullyGrown;
    private Double walkDistance;
    private String petType;
    private String petName;

    // Getters and Setters
}

