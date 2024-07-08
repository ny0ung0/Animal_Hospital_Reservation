package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.restServer.dto.IMemberLoginDto;
import com.example.restServer.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>{

	public Member findByNickname(String nickname);

	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.role='ROLE_HOSPITAL' AND m.status IS NULL", nativeQuery = true)
	public List<IMemberLoginDto> findByStatusNull();
}
