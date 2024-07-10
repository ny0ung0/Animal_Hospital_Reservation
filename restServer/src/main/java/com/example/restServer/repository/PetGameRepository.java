package com.example.restServer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Member;
import com.example.restServer.entity.PetGame;

public interface PetGameRepository extends JpaRepository<PetGame, Long> {

	Optional<PetGame> findByUser(Member user);

	Optional<PetGame> findByUserId(Long user);

	Optional<PetGame> findByUserAndIsFullyGrownFalse(Member user);
}