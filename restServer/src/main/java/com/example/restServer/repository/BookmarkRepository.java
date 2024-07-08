package com.example.restServer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	
	
	@Query(value = "SELECT * FROM bookmark WHERE hospital_id = :hospitalId AND user_id= :userId;", nativeQuery = true)
	Optional<Bookmark> isBookmarked(@Param("hospitalId") Long hospitalId, @Param("userId") Long userId);
}