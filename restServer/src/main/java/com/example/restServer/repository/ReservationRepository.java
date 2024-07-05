package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query(value = "SELECT * FROM reservation WHERE hospital_id = :hospitalId AND status = :status", nativeQuery = true)
	List<Reservation> findAllByHospitalIdAndStatus(@Param("hospitalId")Long hospitalId, @Param("status")String status);
}