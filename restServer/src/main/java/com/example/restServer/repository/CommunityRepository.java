package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}