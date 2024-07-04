package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.PetGame;

public interface PetGameRepository extends JpaRepository<PetGame, Long> {
}