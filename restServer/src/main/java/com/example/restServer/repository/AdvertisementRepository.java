package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Advertisement;
import com.example.restServer.entity.Member;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
	
	

}