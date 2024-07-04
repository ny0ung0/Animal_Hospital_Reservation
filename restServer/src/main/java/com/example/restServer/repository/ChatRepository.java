package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
