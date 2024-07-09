package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.dto.IMemberLoginDto;
import com.example.restServer.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>{

	public Member findByNickname(String nickname);
	@Query(value="Select * from Member where status='승인' and role='ROLE_HOSPITAL' AND address LIKE :address;" , nativeQuery=true)
	public List<Member> findMemberVetList(@Param("address") String address);

	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.role='ROLE_HOSPITAL' AND m.status='대기'", nativeQuery = true)
	public List<IMemberLoginDto> findByStatusWaiting();
	
	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.status = '승인'", nativeQuery = true)
	public List<IMemberLoginDto> findAllAddUsername();
	
	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.status = '승인' AND m.id= :id", nativeQuery = true)
	public IMemberLoginDto findByIdAddUsername(@Param("id") Long id);
	
	 public boolean existsByNickname(String nickname);
}
