package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>{

	public Member findByNickname(String nickname);
	
	@Query(value="Select * from Member where status='승인' and role='ROLE_HOSPITAL' AND address LIKE :address;" , nativeQuery=true)
	public List<Member> findMemberVetList(@Param("address") String address);
}
