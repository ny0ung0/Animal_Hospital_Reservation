package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
