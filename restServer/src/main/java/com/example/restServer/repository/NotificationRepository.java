package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
