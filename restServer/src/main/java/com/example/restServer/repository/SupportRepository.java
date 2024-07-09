package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Support;

public interface SupportRepository extends JpaRepository<Support, Long> {
	
	public List<Support> findAllByCategory(String category);
	
}
