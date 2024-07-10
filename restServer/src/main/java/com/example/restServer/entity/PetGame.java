package com.example.restServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
  private LocalDateTime lastGivenTimeMorning; // 마지막 지급 시간 (7~9시)
  private LocalDateTime lastGivenTimeAfternoon; // 마지막 지급 시간 (12~15시)
  private LocalDateTime lastGivenTimeEvening; // 마지막 지급 시간 (18~21시)

  // Getters and Setters
}
