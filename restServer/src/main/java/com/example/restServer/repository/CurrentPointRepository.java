package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.CurrentPoint;

public interface CurrentPointRepository extends JpaRepository<CurrentPoint, Long> {
}