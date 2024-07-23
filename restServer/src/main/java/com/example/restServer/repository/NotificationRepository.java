package com.example.restServer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByReceiverIdAndIsReadFalse(Long receiverId);
	
	@Query("SELECT COUNT(n) FROM Notification n WHERE n.receiver.id = :memberId AND n.isRead = false")
    Long countByReceiver(@Param("memberId") Long memberId);
	
}
