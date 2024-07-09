package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.UnavailableTime;

public interface UnavailableTimeRepository extends JpaRepository<UnavailableTime, Long> {
	
	List<UnavailableTime> findAllByDoctorId(@Param("doctorId")long doctorId);
}