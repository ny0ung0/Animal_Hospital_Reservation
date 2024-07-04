package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}