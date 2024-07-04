package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
