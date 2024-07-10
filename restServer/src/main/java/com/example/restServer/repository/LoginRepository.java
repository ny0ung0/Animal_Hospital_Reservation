package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.dto.IMemberEditDto;
import com.example.restServer.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	
	public boolean existsByUsername(String username);
	
	public Login findByUsername(String username);

	@Query(value = "delete from login WHERE member_id= :id", nativeQuery = true)
	public void deleteByMemberId(@Param("id")Long id);
	
	
	@Query(value ="SELECT l.username, l.password, m.address, m.phone, m.email, m.name, m.nickname "
			+ "FROM login l "
			+ "LEFT JOIN member m "
			+ "ON l.member_id = m.id "
			+ "WHERE member_id = :memberId;" , nativeQuery = true)
	public List<IMemberEditDto> findByMemberIdEditData(@Param("memberId") String memberId);
	
	
}