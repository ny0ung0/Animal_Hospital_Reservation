package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	
	public boolean existsByUsername(String username);
	
	public Login findByUsername(String username);

	@Query(value = "delete from login WHERE member_id= :id", nativeQuery = true)
	public void deleteByMemberId(@Param("id")Long id);
}