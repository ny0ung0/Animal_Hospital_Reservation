package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Funding;

public interface FundingRepository extends JpaRepository<Funding, Long> {
}