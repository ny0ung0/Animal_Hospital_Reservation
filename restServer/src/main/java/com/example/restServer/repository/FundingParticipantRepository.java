package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.FundingParticipant;

public interface FundingParticipantRepository extends JpaRepository<FundingParticipant, Long> {
}