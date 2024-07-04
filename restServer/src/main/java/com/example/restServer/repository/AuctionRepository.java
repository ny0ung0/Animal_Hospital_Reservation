package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}