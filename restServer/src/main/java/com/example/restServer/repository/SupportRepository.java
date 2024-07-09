package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.restServer.entity.Support;

public interface SupportRepository extends JpaRepository<Support, Long> {
	
	public List<Support> findAllByCategory(String category);
	
	@Query(value = "SELECT * FROM support WHERE category = '불편신고' OR category = '기타문의'", nativeQuery = true)
	public List<Support> findAllByCategoryQna();
}
