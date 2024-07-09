package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
	//해당병원에 해당 유저가 가지고 있는 쿠폰 중 만료가 안되고 사용한적없는 쿠폰 목록 뽑기
	@Query(value="select * from coupon where user_id=:userId and hospital_id=:hospitalId and ISNULL(is_used) AND expiry_date>= now()", nativeQuery=true)
	public List<Coupon> findCouponByUserAndHospital (@Param("userId") Long userId, @Param("hospitalId") Long hospitalId);
}
