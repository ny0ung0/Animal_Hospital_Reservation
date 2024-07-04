package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}