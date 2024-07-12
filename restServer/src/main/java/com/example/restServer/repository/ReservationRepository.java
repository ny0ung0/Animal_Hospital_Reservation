package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Doctor;
import com.example.restServer.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query(value = "SELECT * FROM reservation WHERE hospital_id = :memberId AND status = :status", nativeQuery = true)
	List<Reservation> findAllByHospitalIdAndStatus(@Param("memberId")Long memberId, @Param("status")String status);
	
	@Query(value = "SELECT AVG(RATING) FROM reservation WHERE hospital_id = :hospitalId AND !ISNULL(RATING);", nativeQuery = true)
	Long findAvgReview(@Param("hospitalId") Long hospitalId);

	@Query(value = "SELECT * FROM reservation WHERE doctor_id = :doctorId", nativeQuery = true)
	List<Reservation> findAllByDoctorId(@Param("doctorId")Long doctorId);
	
	@Query(value = "SELECT * FROM reservation WHERE hospital_id= :doctorId AND !isnull(review) ORDER BY updated_at DESC", nativeQuery = true)
	List<Reservation> findReservWithReview (@Param("doctorId")Long doctorId);
	
}