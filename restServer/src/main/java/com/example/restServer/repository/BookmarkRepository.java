package com.example.restServer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restServer.dto.BookmarkDto;
import com.example.restServer.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	
	
	@Query(value = "SELECT * FROM bookmark WHERE hospital_id = :hospitalId AND user_id= :userId;", nativeQuery = true)
	Optional<Bookmark> isBookmarked(@Param("hospitalId") Long hospitalId, @Param("userId") Long userId);
	
	@Query(value ="SELECT b.id,b.hospital_id,b.user_id, m.hospital_name, m.address, m.phone FROM bookmark b LEFT JOIN member m ON m.id = b.hospital_id WHERE b.user_id= :userId;", nativeQuery = true)
	List<BookmarkDto> findAllByUserId(@Param("userId") Long userId);

}