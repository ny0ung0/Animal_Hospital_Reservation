package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.UnavailableTime;

public interface UnavailableTimeRepository extends JpaRepository<UnavailableTime, Long> {
}