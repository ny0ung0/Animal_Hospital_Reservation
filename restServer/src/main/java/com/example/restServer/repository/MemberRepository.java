package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.dto.IJoinCountDto;
import com.example.restServer.dto.IMemberLoginDto;
import com.example.restServer.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>{

	public Member findByNickname(String nickname);
	@Query(value="Select * from Member where status='승인' and role='ROLE_HOSPITAL' AND address LIKE :address;" , nativeQuery=true)
	public List<Member> findMemberVetList(@Param("address") String address);

	@Query(value="Select * from Member where status='승인' and role='ROLE_HOSPITAL' AND address LIKE :address and hospital_name=:hospitalName;" , nativeQuery=true)
	public List<Member> findMemberVet(@Param("address") String address, @Param("hospitalName") String hospitalName);
	
	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.role='ROLE_WAIT' AND m.status='대기'", nativeQuery = true)
	public List<IMemberLoginDto> findByStatusWaiting();
	
	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.role='ROLE_USER' AND m.status = '승인'", nativeQuery = true)
	public List<IMemberLoginDto> findAllUserAddUsername();
	
	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.role='ROLE_HOSPITAL' AND m.status = '승인'", nativeQuery = true)
	public List<IMemberLoginDto> findAllHospitalAddUsername();
	
	@Query(value = "SELECT m.*, l.username, l.id AS login_id FROM member m JOIN login l ON m.id = l.member_id WHERE m.status = '승인' AND m.id= :id", nativeQuery = true)
	public IMemberLoginDto findByIdAddUsername(@Param("id") Long id);
	
	
	
	public boolean existsByNickname(String nickname);
	 
	@Query(value = "SELECT m.*, l.username, l.id AS login_id " +
            "FROM member m JOIN login l ON m.id = l.member_id " +
            "WHERE m.role = 'ROLE_HOSPITAL' " +
            "AND m.status = '승인' " +
            "AND m.hospital_name LIKE %:keyword%", nativeQuery = true)
	public List<IMemberLoginDto> findAllHospitalAddUsernameByKeyword(@Param("keyword")String keyword);
	
	@Query(value = "SELECT m.*, l.username, l.id AS login_id " +
            "FROM member m JOIN login l ON m.id = l.member_id " +
            "WHERE m.role = 'ROLE_USER' " +
            "AND m.status = '승인' " +
            "AND m.name LIKE %:keyword%", nativeQuery = true)
	public List<IMemberLoginDto> findAllUserAddUsernameByKeyword(@Param("keyword")String keyword);
	
	@Query(value = "select exists( select 1 from member where nickname= :nickname and id <> :member_id)", nativeQuery = true)
	public int existsByNicknameEdit(@Param("nickname")String nickname,@Param("member_id")String member_id);
	
	
	@Query(value = "SELECT COUNT(*) from member WHERE role=:type", nativeQuery = true)
	public Long findAllCountMemberType(@Param("type")String type);
	
	
	
	@Query(value = "SELECT DATE(created_at) AS join_date, COUNT(*) AS member_count"
			+ "	FROM member"
			+ "	WHERE created_at >= CURDATE() - INTERVAL 1 WEEK and role='ROLE_USER'"
			+ "	GROUP BY DATE(created_at)"
			+ "	ORDER BY join_date", nativeQuery = true)
	public List<IJoinCountDto> userCountJoinWeek();
	
	
	@Query(value = "SELECT DATE(created_at) AS join_date, COUNT(*) AS member_count"
			+ "	FROM member"
			+ "	WHERE created_at >= CURDATE() - INTERVAL 1 WEEK and role='ROLE_HOSPITAL'"
			+ "	GROUP BY DATE(created_at)"
			+ "	ORDER BY join_date", nativeQuery = true)
	public List<IJoinCountDto> hospitalCountJoinWeek();
	 
}
