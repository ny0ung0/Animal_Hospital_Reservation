package com.example.restServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restServer.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}