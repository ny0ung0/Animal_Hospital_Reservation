package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
