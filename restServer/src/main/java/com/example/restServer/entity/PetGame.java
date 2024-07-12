package com.example.restServer.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetGame extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("Id")
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
  private LocalDateTime lastGivenTimeMorning; // 마지막 지급 시간 (7~9시)
  private LocalDateTime lastGivenTimeAfternoon; // 마지막 지급 시간 (12~15시)
  private LocalDateTime lastGivenTimeEvening; // 마지막 지급 시간 (18~21시)

  // Getters and Setters
}
