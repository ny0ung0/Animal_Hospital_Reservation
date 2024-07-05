package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	
	public boolean existsByUsername(String username);
}